package com.ohbs.hotelmgt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ohbs.hotelmgt.exception.DuplicateHotelNameException;
import com.ohbs.hotelmgt.exception.HotelNotFoundException;
import com.ohbs.hotelmgt.exception.InvalidRatingException;
import com.ohbs.hotelmgt.model.Hotel;
import com.ohbs.hotelmgt.model.HotelImage;
import com.ohbs.hotelmgt.repository.HotelRepository;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public Hotel createHotel(Hotel hotel) {
        if (hotelRepository.findByName(hotel.getName()) != null) {
            throw new DuplicateHotelNameException("Hotel with name '" + hotel.getName() + "' already exists.");
        }

        validateRating(hotel.getRating());

        if (hotel.getImages() != null) {
            for (HotelImage image : hotel.getImages()) {
                image.setHotel(hotel); // 🔗 Link images to hotel
            }
        }

        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel getHotelById(long id) {
        return hotelRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel with ID " + id + " not found"));
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findByIsDeletedFalse();
    }

    @Override
    public Hotel updateHotel(Hotel updatedHotel) {
        // ⛔ Prevent update of deleted hotel
        Hotel existingHotel = hotelRepository.findByIdAndIsDeletedFalse(updatedHotel.getId())
                .orElseThrow(() -> new HotelNotFoundException("Hotel with ID " + updatedHotel.getId() + " not found or has been deleted."));

        // Check for name conflict
        Hotel duplicate = hotelRepository.findByName(updatedHotel.getName());
        if (!existingHotel.getName().equals(updatedHotel.getName())
                && duplicate != null && !duplicate.getId().equals(existingHotel.getId())) {
            throw new DuplicateHotelNameException("Another hotel with name '" + updatedHotel.getName() + "' already exists.");
        }

        validateRating(updatedHotel.getRating());

        // Basic field updates
        existingHotel.setName(updatedHotel.getName());
        existingHotel.setLocation(updatedHotel.getLocation());
        existingHotel.setDescription(updatedHotel.getDescription());
        existingHotel.setRating(updatedHotel.getRating());

        // Replace images
        existingHotel.getImages().clear();
        if (updatedHotel.getImages() != null) {
            for (HotelImage image : updatedHotel.getImages()) {
                image.setHotel(existingHotel);
                existingHotel.getImages().add(image);
            }
        }

        return hotelRepository.save(existingHotel);
    }

    @Override
    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel with ID " + id + " not found"));
        hotel.setDeleted(true);  // Soft delete
        hotelRepository.save(hotel);
    }

    private void validateRating(double rating) {
        if (rating < 1.0 || rating > 5.0) {
            throw new InvalidRatingException("Rating must be between 1.0 and 5.0.");
        }
    }
}
