package com.ohbs.hotelmgt.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ohbs.hotelmgt.model.Hotel;
import com.ohbs.hotelmgt.model.HotelImage;
import com.ohbs.hotelmgt.repository.HotelImageRepository;
import com.ohbs.hotelmgt.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ImageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelImageRepository hotelImageRepository;

    /**
     * Upload multiple images for a given hotel.
     * Only allows active (non-deleted) hotels.
     */
    public List<String> uploadImages(MultipartFile[] files, Long hotelId) throws IOException {
        Hotel hotel = hotelRepository.findByIdAndIsDeletedFalse(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found or is deleted"));

        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
                continue; // Skip invalid files
            }
            if (file.getSize() > MAX_FILE_SIZE) {
                continue; // Skip too large files
            }

            // Upload image to Cloudinary under folder for this hotel
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "ohbs/hotels/" + hotelId,
                    "public_id", UUID.randomUUID().toString(),
                    "resource_type", "image"
            ));

            String imageUrl = uploadResult.get("secure_url").toString();

            // Save image info in DB linked to hotel
            HotelImage hotelImage = new HotelImage();
            hotelImage.setHotel(hotel);
            hotelImage.setImageUrl(imageUrl);
            hotelImageRepository.save(hotelImage);

            uploadedUrls.add(imageUrl);
        }

        return uploadedUrls;
    }

    /**
     * Update an existing image by ID.
     * Checks if image and its hotel exist and are active.
     */
    public String updateImage(Long imageId, MultipartFile newFile) throws IOException {
        if (newFile.isEmpty() || !newFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image file");
        }
        if (newFile.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds limit of 5MB");
        }

        HotelImage image = hotelImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));

        Hotel hotel = image.getHotel();
        if (hotel == null || hotel.getIsDeleted()) {
            throw new IllegalStateException("Cannot update image of a deleted hotel");
        }

        // Upload new image to Cloudinary
        Map<?, ?> uploadResult = cloudinary.uploader().upload(newFile.getBytes(), ObjectUtils.asMap(
                "folder", "ohbs/hotels/" + hotel.getId(),
                "public_id", UUID.randomUUID().toString(),
                "resource_type", "image"
        ));

        String newUrl = uploadResult.get("secure_url").toString();

        // Optional: Delete old image from Cloudinary using public_id if stored
        // cloudinary.uploader().destroy(oldPublicId, ObjectUtils.emptyMap());

        image.setImageUrl(newUrl);
        hotelImageRepository.save(image);

        return newUrl;
    }

    /**
     * Delete an image by ID.
     * Checks if image and hotel are active.
     */
    public void deleteImage(Long imageId) throws IOException {
        HotelImage image = hotelImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));

        Hotel hotel = image.getHotel();
        if (hotel == null || hotel.getIsDeleted()) {
            throw new IllegalStateException("Cannot delete image of a deleted hotel");
        }

        // Optional: Delete image from Cloudinary if you store public_id
        // cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());

        hotelImageRepository.delete(image);
    }

    /**
     * Get all active image URLs for a given hotel.
     * Only for active hotels.
     */
    public List<String> getImagesByHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.findByIdAndIsDeletedFalse(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found or is deleted"));

        List<HotelImage> images = hotelImageRepository.findByHotelId(hotelId);

        List<String> urls = new ArrayList<>();
        for (HotelImage image : images) {
            urls.add(image.getImageUrl());
        }
        return urls;
    }
}
