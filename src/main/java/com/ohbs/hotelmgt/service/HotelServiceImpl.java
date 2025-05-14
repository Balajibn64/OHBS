package com.ohbs.hotelmgt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ohbs.hotelmgt.exception.DuplicateHotelNameException;
import com.ohbs.hotelmgt.exception.HotelNotFoundException;
import com.ohbs.hotelmgt.exception.InvalidRatingException;
import com.ohbs.hotelmgt.model.Hotel;
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

        if (hotel.getRating() < 0 || hotel.getRating() > 5) {
            throw new InvalidRatingException("Rating must be between 0 and 5.");
        }

        // You could add logic for image validation here if needed
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
    public Hotel updateHotel(Hotel hotels) {
        Hotel existingHotel = hotelRepository.findById(hotels.getId())
                .orElseThrow(() -> new HotelNotFoundException("Hotel with ID " + hotels.getId() + " not found"));

        if (!existingHotel.getName().equals(hotels.getName())
                && hotelRepository.findByName(hotels.getName()) != null) {
            throw new DuplicateHotelNameException("Another hotel with name '" + hotels.getName() + "' already exists.");
        }

        if (hotels.getRating() < 0 || hotels.getRating() > 5) {
            throw new InvalidRatingException("Rating must be between 0 and 5.");
        }

        existingHotel.setName(hotels.getName());
        existingHotel.setLocation(hotels.getLocation());
        existingHotel.setDescription(hotels.getDescription());
        existingHotel.setRating(hotels.getRating());
//        existingHotel.setImageUrl(hotels.getImageUrl());

        return hotelRepository.save(existingHotel);
    }

    @Override
    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel with ID " + id + " not found"));
        hotel.setDeleted(true);  // Soft delete
        hotelRepository.save(hotel);
    }
}
