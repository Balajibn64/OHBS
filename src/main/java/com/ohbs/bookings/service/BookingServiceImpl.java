package com.ohbs.bookings.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ohbs.bookings.dto.BookingRequestDto;
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

    @Override
    public Booking createBooking(BookingRequestDto dto) {
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
        
        // Important: status is set internally, not from client input
        booking.setStatus(BookingStatus.BOOKED);

        return bookingRepo.save(booking);
    }

    @Override
    public Booking getBooking(Long id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public List<Booking> getBookingsByCustomer(Long customerId) {
        return bookingRepo.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsByRoom(Long roomId) {
        return bookingRepo.findByRoomId(roomId);
    }

    @Override
    public Booking updateBooking(Long id, BookingRequestDto dto) {
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

        // Status cannot be changed here by client
        return bookingRepo.save(booking);
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
        // TODO: trigger refund/payment reversal if applicable
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
}
