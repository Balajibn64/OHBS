package com.ohbs.hotelmgt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ohbs.Customer.exception.UnauthorizedAccessException;
import com.ohbs.auth.service.AuthService;
import com.ohbs.hotelmgt.dto.HotelRequestDTO;
import com.ohbs.hotelmgt.dto.HotelResponseDTO;
import com.ohbs.hotelmgt.exception.DuplicateHotelNameException;
import com.ohbs.hotelmgt.exception.HotelNotFoundException;
import com.ohbs.hotelmgt.exception.InvalidRatingException;
import com.ohbs.hotelmgt.model.Hotel;
import com.ohbs.hotelmgt.repository.HotelRepository;
import com.ohbs.manager.model.Manager;
import com.ohbs.manager.service.ManagerService;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AuthService authService;
    

    @Override
    public HotelResponseDTO createHotel(HotelRequestDTO dto, Long managerId) {
        if (hotelRepository.findByName(dto.getName()) != null) {
            throw new DuplicateHotelNameException("Hotel with name '" + dto.getName() + "' already exists.");
        }

        if (dto.getRating() < 1.0 || dto.getRating() > 5.0) {
            throw new InvalidRatingException("Rating must be between 1.0 and 5.0.");
        }

        Manager manager = authService.getManagerById(managerId);

        Hotel hotel = Hotel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .rating(dto.getRating())
                .manager(manager)
                .isDeleted(false)
                .build();

        return HotelResponseDTO.fromEntity(hotelRepository.save(hotel));
    }

    @Override
    public HotelResponseDTO getHotelById(long id, Long managerId) {
        Hotel hotel = getActiveHotelOrThrow(id);
        validateOwnership(hotel, managerId);
        return HotelResponseDTO.fromEntity(hotel);
    }

    @Override
    public List<HotelResponseDTO> getAllHotelsByManager(Long managerId) {
        return hotelRepository.findByManagerIdAndIsDeletedFalse(managerId)
                .stream()
                .map(HotelResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public HotelResponseDTO updateHotel(Long id, HotelRequestDTO dto, Long managerId) {
        Hotel hotel = getActiveHotelOrThrow(id);
        validateOwnership(hotel, managerId);

        if (!hotel.getName().equalsIgnoreCase(dto.getName())
                && hotelRepository.findByName(dto.getName()) != null) {
            throw new DuplicateHotelNameException("Another hotel with name '" + dto.getName() + "' already exists.");
        }

        if (dto.getRating() < 1.0 || dto.getRating() > 5.0) {
            throw new InvalidRatingException("Rating must be between 1.0 and 5.0.");
        }

        hotel.setName(dto.getName());
        hotel.setDescription(dto.getDescription());
        hotel.setLocation(dto.getLocation());
        hotel.setRating(dto.getRating());

        return HotelResponseDTO.fromEntity(hotelRepository.save(hotel));
    }

    @Override
    public void deleteHotel(Long id, Long managerId) {
        Hotel hotel = getActiveHotelOrThrow(id);
        validateOwnership(hotel, managerId);
        hotel.setDeleted(true);
        hotelRepository.save(hotel);
    }

    // ========= Private Helpers ===========

    private Hotel getActiveHotelOrThrow(Long id) {
        return hotelRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel with ID " + id + " not found."));
    }

    private void validateOwnership(Hotel hotel, Long managerId) {
        if (!hotel.getManager().getId().equals(managerId)) {
            throw new UnauthorizedAccessException("You are not authorized to access this hotel.");
        }
    }
}
