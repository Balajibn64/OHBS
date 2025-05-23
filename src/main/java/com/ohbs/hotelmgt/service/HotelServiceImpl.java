package com.ohbs.hotelmgt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ohbs.Customer.exception.UnauthorizedAccessException;
import com.ohbs.auth.service.AuthService;
import com.ohbs.hotelmgt.dto.HotelFilterDTO;
import com.ohbs.hotelmgt.dto.HotelRequestDTO;
import com.ohbs.hotelmgt.dto.HotelResponseDTO;
import com.ohbs.hotelmgt.exception.DuplicateHotelNameException;
import com.ohbs.hotelmgt.exception.HotelNotFoundException;
import com.ohbs.hotelmgt.exception.InvalidRatingException;
import com.ohbs.hotelmgt.model.Hotel;
import com.ohbs.hotelmgt.repository.HotelRepository;
import com.ohbs.manager.model.Manager;
import com.ohbs.manager.repository.ManagerRepository;
import com.ohbs.security.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public List<HotelResponseDTO> getHotelByFilter(HotelFilterDTO dto) {
        if(dto.getSortBy().equalsIgnoreCase("asc")) {
        	System.out.println(dto.getSortBy().equalsIgnoreCase("asc"));
            List<HotelResponseDTO> hotels = hotelRepository.findByLocationOrderByPriceAsc(dto.getLocation())
                    .stream()
                    .map(HotelResponseDTO::fromEntity)
                    .collect(Collectors.toList());
            System.out.println(hotelRepository.findByLocationOrderByPriceAsc(dto.getLocation()));
            return hotels;
        }else {
        	List<HotelResponseDTO> hotels = hotelRepository.findByLocationOrderByPriceDesc(dto.getLocation())
                    .stream()
                    .map(HotelResponseDTO::fromEntity)
                    .collect(Collectors.toList());
            return hotels;
        }
    }

    @Override
    public HotelResponseDTO createHotel(HotelRequestDTO dto, HttpServletRequest request) {
        if (hotelRepository.findByName(dto.getName()) != null) {
            throw new DuplicateHotelNameException("Hotel with name '" + dto.getName() + "' already exists.");
        }

        if (dto.getRating() < 1.0 || dto.getRating() > 5.0) {
            throw new InvalidRatingException("Rating must be between 1.0 and 5.0.");
        }

        // Extract userId from token using JwtUtil directly
        Long userId = extractManagerIdFromRequest(request);

        // Fetch manager by userId directly using ManagerRepository
        Manager manager = managerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Manager not found for user ID: " + userId));

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
    public HotelResponseDTO getHotelById(long id, HttpServletRequest request) {
        Hotel hotel = getActiveHotelOrThrow(id);
        Long managerId = extractManagerIdFromRequest(request);
        validateOwnership(hotel, managerId);
        return HotelResponseDTO.fromEntity(hotel);
    }

    @Override
    public List<HotelResponseDTO> getAllHotelsByManager(HttpServletRequest request) {
    	Long managerId = extractManagerIdFromRequest(request);
        return hotelRepository.findByManagerIdAndIsDeletedFalse(managerId)
                .stream()
                .map(HotelResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public HotelResponseDTO updateHotel(Long id, HotelRequestDTO dto, HttpServletRequest request) {
        Hotel hotel = getActiveHotelOrThrow(id);
        Long managerId = extractManagerIdFromRequest(request);
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
    public void deleteHotel(Long id, HttpServletRequest request) {
        Hotel hotel = getActiveHotelOrThrow(id);
        Long managerId = extractManagerIdFromRequest(request);
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

    //Extract managerId from request
    private Long extractManagerIdFromRequest(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        throw new UnauthorizedAccessException("Missing or invalid Authorization header");
    }
    String token = authHeader.substring(7);

    // Extract userId from token
    Long userId = jwtUtil.extractUserId(token);

    // Fetch manager by userId
    return managerRepository.findByUserId(userId)
            .map(Manager::getId)
            .orElseThrow(() -> new RuntimeException("Manager not found for user ID: " + userId));
}

}
