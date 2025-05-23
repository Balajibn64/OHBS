package com.ohbs.hotelmgt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ohbs.hotelmgt.model.Hotel;
import com.ohbs.hotelmgt.model.HotelImage;
import com.ohbs.hotelmgt.repository.HotelImageRepository;
import com.ohbs.hotelmgt.repository.HotelRepository;
import com.ohbs.room.model.Room;
import com.ohbs.room.model.RoomImage;
import com.ohbs.room.repository.RoomImageRepository;
import com.ohbs.room.repository.RoomRepository;

@Service
public class ImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelImageRepository hotelImageRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomImageRepository roomImageRepository;

    // ===================== HOTEL IMAGE METHODS =====================

    // Upload multiple images for a hotel
    public List<String> uploadImages(MultipartFile[] files, Long hotelId) throws IOException {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if (optionalHotel.isEmpty()) {
            throw new IllegalArgumentException("Hotel not found with id: " + hotelId);
        }
        Hotel hotel = optionalHotel.get();
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty() || !file.getContentType().startsWith("image/")) continue;
            if (file.getSize() > 5 * 1024 * 1024) continue;

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder",
                    "ohbs/hotels/" + hotelId, "public_id", UUID.randomUUID().toString(), "resource_type", "image"));

            String imageUrl = uploadResult.get("secure_url").toString();
            imageUrls.add(imageUrl);

            HotelImage hotelImage = new HotelImage();
            hotelImage.setImageUrl(imageUrl);
            hotelImage.setHotel(hotel);
            hotelImageRepository.save(hotelImage);
        }
        return imageUrls;
    }

    // Get images by hotelId
    public List<String> getImagesByHotelId(Long hotelId) {
        return hotelImageRepository.findByHotelId(hotelId).stream()
                .map(HotelImage::getImageUrl)
                .toList();
    }

    // Update a hotel image by its ID
    public String updateImage(Long imageId, MultipartFile newFile) throws IOException {
        HotelImage image = hotelImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));

        Map uploadResult = cloudinary.uploader().upload(newFile.getBytes(),
                ObjectUtils.asMap("folder", "ohbs/hotels/" + image.getHotel().getId(), "public_id",
                        UUID.randomUUID().toString(), "resource_type", "image"));

        String newUrl = uploadResult.get("secure_url").toString();
        image.setImageUrl(newUrl);
        hotelImageRepository.save(image);
        return newUrl;
    }

    // Delete a hotel image by its ID
    public void deleteImage(Long imageId) throws IOException {
        HotelImage image = hotelImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));
		hotelImageRepository.delete(image);
    }

    // ===================== ROOM IMAGE METHODS =====================

    // Upload multiple images for a room
    public List<String> uploadRoomImages(MultipartFile[] files, Long roomId) throws IOException {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isEmpty()) {
            throw new IllegalArgumentException("Room not found with id: " + roomId);
        }
        Room room = optionalRoom.get();
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty() || !file.getContentType().startsWith("image/")) continue;
            if (file.getSize() > 5 * 1024 * 1024) continue;

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder",
                    "ohbs/rooms/" + roomId, "public_id", UUID.randomUUID().toString(), "resource_type", "image"));

            String imageUrl = uploadResult.get("secure_url").toString();
            imageUrls.add(imageUrl);

            RoomImage roomImage = new RoomImage();
            roomImage.setImageUrl(imageUrl);
            roomImage.setRoom(room);
            roomImageRepository.save(roomImage);
        }
        return imageUrls;
    }

    // Get images by roomId
    public List<String> getImagesByRoomId(Long roomId) {
        return roomImageRepository.findByRoomId(roomId)
                .stream()
                .map(RoomImage::getImageUrl)
                .toList();
    }

    // Update a room image by its ID
    public String updateRoomImage(Long imageId, MultipartFile newFile) throws IOException {
        RoomImage image = roomImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Room image not found"));

        Map uploadResult = cloudinary.uploader().upload(newFile.getBytes(),
                ObjectUtils.asMap("folder", "ohbs/rooms/" + image.getRoom().getId(), "public_id",
                        UUID.randomUUID().toString(), "resource_type", "image"));

        String newUrl = uploadResult.get("secure_url").toString();
        image.setImageUrl(newUrl);
        roomImageRepository.save(image);
        return newUrl;
    }

    // Delete a room image by its ID
    public void deleteRoomImage(Long imageId) throws IOException {
        RoomImage image = roomImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Room image not found"));
        roomImageRepository.delete(image);
    }

    // ===================== PROFILE IMAGE METHOD =====================

    public String uploadProfileImage(MultipartFile file, Long userId) throws IOException {
        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image file");
        }

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder",
                "ohbs/customers/" + userId, "public_id", UUID.randomUUID().toString(), "resource_type", "image"));

        return uploadResult.get("secure_url").toString();
    }
}
