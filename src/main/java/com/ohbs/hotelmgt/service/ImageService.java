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

    // ✅ Get images by hotelId (only if hotel is not deleted)
    public List<String> getImagesByHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.findByIdAndIsDeletedFalse(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found or is deleted"));

        return hotelImageRepository.findActiveImagesByHotelId(hotelId)
                .stream()
                .map(HotelImage::getImageUrl)
                .toList();
    }

    // ✅ Update an image only if its hotel is active
    public String updateImage(Long imageId, MultipartFile newFile) throws IOException {
        HotelImage image = hotelImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));

        if (image.getHotel() == null || image.getHotel().getIsDeleted()) {
            throw new IllegalStateException("Cannot update image of a deleted hotel");
        }

        Map uploadResult = cloudinary.uploader().upload(newFile.getBytes(), ObjectUtils.asMap(
                "folder", "ohbs/hotels/" + image.getHotel().getId(),
                "public_id", UUID.randomUUID().toString(),
                "resource_type", "image"
        ));

        String newUrl = uploadResult.get("secure_url").toString();
        image.setImageUrl(newUrl);
        hotelImageRepository.save(image);

        return newUrl;
    }

    // ✅ Delete an image only if its hotel is active
    public void deleteImage(Long imageId) throws IOException {
        HotelImage image = hotelImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));

        if (image.getHotel() == null || image.getHotel().getIsDeleted()) {
            throw new IllegalStateException("Cannot delete image of a deleted hotel");
        }

        hotelImageRepository.delete(image);
    }

    // ✅ Upload images only if hotel is active
    public List<String> uploadImages(MultipartFile[] files, Long hotelId) throws IOException {
        Hotel hotel = hotelRepository.findByIdAndIsDeletedFalse(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found or is deleted"));

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
                continue;
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                continue;
            }

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "ohbs/hotels/" + hotelId,
                    "public_id", UUID.randomUUID().toString(),
                    "resource_type", "image"
            ));

            String imageUrl = uploadResult.get("secure_url").toString();
            imageUrls.add(imageUrl);

            HotelImage hotelImage = new HotelImage();
            hotelImage.setImageUrl(imageUrl);
            hotelImage.setHotel(hotel);

            hotelImageRepository.save(hotelImage);
        }

        return imageUrls;
    }
}
