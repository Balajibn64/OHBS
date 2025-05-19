package com.ohbs.bookings.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ohbs.bookings.dto.BookingRequestDto;
import com.ohbs.bookings.dto.BookingResponseDto;
import com.ohbs.bookings.model.Booking;
import com.ohbs.bookings.model.BookingStatus;
import com.ohbs.bookings.repository.BookingRepository;
import com.ohbs.common.exception.BadRequestException;
import com.ohbs.common.exception.ResourceNotFoundException;
import com.ohbs.common.model.User;
import com.ohbs.common.repository.UserRepository;
import com.ohbs.room.model.Room;
import com.ohbs.room.repository.RoomRepository;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoomRepository roomRepo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public BookingResponseDto createBooking(BookingRequestDto dto) {
        User customer = userRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Room room = roomRepo.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setTotalPrice(dto.getTotalPrice());
        booking.setStatus(BookingStatus.BOOKED);

        Booking saved = bookingRepo.save(booking);
        return mapToDto(saved);
    }

    @Override
    public BookingResponseDto getBooking(Long id) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return mapToDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingsByCustomer(Long customerId) {
        return bookingRepo.findByCustomerId(customerId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getBookingsByRoom(Long roomId) {
        return bookingRepo.findByRoomId(roomId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public BookingResponseDto updateBooking(Long id, BookingRequestDto dto) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BadRequestException("Cannot update a cancelled or completed booking.");
        }

        User customer = userRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Room room = roomRepo.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setTotalPrice(dto.getTotalPrice());

        Booking saved = bookingRepo.save(booking);
        return mapToDto(saved);
    }

    @Override
    public void cancelBooking(Long id) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CHECKED_IN) {
            throw new BadRequestException("Cannot cancel booking after check-in.");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepo.save(booking);
    }

    @Override
    public void checkInBooking(Long id) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.BOOKED) {
            throw new BadRequestException("Only booked reservations can be checked in.");
        }

        booking.setStatus(BookingStatus.CHECKED_IN);
        bookingRepo.save(booking);
    }

    @Override
    public void checkOutBooking(Long id) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BadRequestException("Only checked-in bookings can be checked out.");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepo.save(booking);
    }

    // 🔁 Mapping method
    private BookingResponseDto mapToDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .customerId(booking.getCustomer().getId())
                .customerName(booking.getCustomer().getUsername() )
                .roomId(booking.getRoom().getId())
                .roomNumber(booking.getRoom().getRoomNumber())
                .roomType(booking.getRoom().getRoomType())
                .hotelName(booking.getRoom().getHotel().getName())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalPrice(booking.getTotalPrice())
                .bookingStatus(booking.getStatus())
                .createdAt(booking.getCreatedAt().format(formatter))
                .updatedAt(booking.getUpdatedAt().format(formatter))
                .build();
    }
}
