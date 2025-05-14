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

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelImageRepository hotelImageRepository;

    // Get images by hotelId
    public List<String> getImagesByHotelId(Long hotelId) {
        return hotelImageRepository.findByHotelId(hotelId)
                .stream()
                .map(HotelImage::getImageUrl)
                .toList();
    }

    // Update an image by its ID
    public String updateImage(Long imageId, MultipartFile newFile) throws IOException {
        HotelImage image = hotelImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));

        // Upload new image to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(newFile.getBytes(), ObjectUtils.asMap(
                "folder", "ohbs/hotels/" + image.getHotel().getId(),
                "public_id", UUID.randomUUID().toString(),
                "resource_type", "image"
        ));

        // Optional: Delete the old image from Cloudinary if public_id is stored
        // cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());

        String newUrl = uploadResult.get("secure_url").toString();
        image.setImageUrl(newUrl);
        hotelImageRepository.save(image);

        return newUrl;
    }

    // Delete an image by its ID
    public void deleteImage(Long imageId) throws IOException {
        HotelImage image = hotelImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));

        // Optional: Delete from Cloudinary
        // cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());

        hotelImageRepository.delete(image);
    }

    // Upload multiple images for a hotel
    public List<String> uploadImages(MultipartFile[] files, Long hotelId) throws IOException {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);

        if (optionalHotel.isEmpty()) {
            throw new IllegalArgumentException("Hotel not found with id: " + hotelId);
        }

        Hotel hotel = optionalHotel.get();
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
                continue;
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                continue;
            }

            // Upload the file to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "ohbs/hotels/" + hotelId,
                    "public_id", UUID.randomUUID().toString(),
                    "resource_type", "image"
            ));

            String imageUrl = uploadResult.get("secure_url").toString();
            imageUrls.add(imageUrl);

            // Create a new HotelImage entity and associate it with the hotel
            HotelImage hotelImage = new HotelImage();
            hotelImage.setImageUrl(imageUrl);
            hotelImage.setHotel(hotel);

            // Save the HotelImage to the database
            hotelImageRepository.save(hotelImage);
        }

        return imageUrls;
    }
}
