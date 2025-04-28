package ru.practicum.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserDto;
import ru.practicum.user.UserMapper;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;
    private final UserMapper userMapper;
    private final UserService userService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              ItemRepository itemRepository,
                              BookingMapper bookingMapper,
                              UserMapper userMapper,
                              UserService userService) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.bookingMapper = bookingMapper;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto, Long userId) {
        UserDto userDto = userService.getUserById(userId);
        if (userDto == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        User user = userMapper.toEntity(userDto);

        Item item = itemRepository.findById(bookingDto.getId());
        if (item == null) {
            throw new NotFoundException("Предмет не найден");
        }

        if (!item.isAvailable()) {
            throw new IllegalStateException("Предмет недоступен для бронирования");
        }

        Booking booking = bookingMapper.toEntity(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new NotFoundException("Бронирование не найдено");
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId) {
        return bookingRepository.findByBookerId(userId)
                .stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getItemBookings(Long itemId) {
        return bookingRepository.findByItemId(itemId)
                .stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto updateBookingStatus(Long bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new NotFoundException("Бронирование не найдено");
        }
        booking.setStatus(newStatus);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new NotFoundException("Бронирование не найдено");
        }
        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }

    public boolean checkDateOverlap(LocalDateTime start, LocalDateTime end, Long itemId) {
        List<Booking> existingBookings = bookingRepository.findByItemId(itemId);

        for (Booking existingBooking : existingBookings) {
            if (datesOverlap(
                    start,
                    end,
                    existingBooking.getStartDate(),
                    existingBooking.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    private boolean datesOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return !(end1.isBefore(start2) || start1.isAfter(end2));
    }
}
