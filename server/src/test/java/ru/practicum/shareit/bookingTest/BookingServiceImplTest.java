package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User owner;
    private User booker;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        owner = User.builder().id(1L).name("Owner").email("owner@example.com").build();
        booker = User.builder().id(2L).name("Booker").email("booker@example.com").build();

        item = Item.builder().id(10L).name("Item").owner(owner).available(true).build();

        booking = Booking.builder()
                .id(100L)
                .item(item)
                .booker(booker)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void patchBooking_approve_success() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseBookingDto response = bookingService.patchBooking(owner.getId(), booking.getId(), true);

        assertEquals(BookingStatus.APPROVED, response.getStatus());
        assertEquals(booking.getId(), response.getId());
        verify(bookingRepository).save(booking);
    }


    @Test
    void patchBooking_reject_success() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseBookingDto response = bookingService.patchBooking(owner.getId(), booking.getId(), false);

        assertEquals(BookingStatus.REJECTED, response.getStatus());
        assertEquals(booking.getId(), response.getId());
        verify(bookingRepository).save(booking);
    }

    @Test
    void patchBooking_bookingNotFound_throwsNotFoundException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.patchBooking(owner.getId(), 999L, true));
        assertTrue(ex.getMessage().contains("не найдено"));
    }

    @Test
    void patchBooking_bookerIsOwner_throwsNotFoundException() {
        booking.setBooker(owner);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.patchBooking(owner.getId(), booking.getId(), true));
        assertTrue(ex.getMessage().contains("Наглый букер"));
    }

    @Test
    void patchBooking_userNotOwner_throwsIncorrectUserException() {
        booking.getItem().setOwner(owner);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        Long wrongUserId = 999L;
        IncorrectUserException ex = assertThrows(IncorrectUserException.class,
                () -> bookingService.patchBooking(wrongUserId, booking.getId(), true));
        assertTrue(ex.getMessage().contains("не имеет прав"));
    }

    @Test
    void patchBooking_alreadyApproved_throwsValidationException() {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.patchBooking(owner.getId(), booking.getId(), true));
        assertTrue(ex.getMessage().contains("уже одобрена"));
    }

    @Test
    void getBookingById_ownerOrBooker_success() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        ResponseBookingDto resp1 = bookingService.getBookingById(booker.getId(), booking.getId());
        assertEquals(booking.getId(), resp1.getId());

        ResponseBookingDto resp2 = bookingService.getBookingById(owner.getId(), booking.getId());
        assertEquals(booking.getId(), resp2.getId());
    }

    @Test
    void getBookingById_notOwnerAndNotBooker_throwsException() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        Long strangerId = 999L;
        NotOwnerAndNotBookerException ex = assertThrows(NotOwnerAndNotBookerException.class,
                () -> bookingService.getBookingById(strangerId, booking.getId()));
        assertTrue(ex.getMessage().contains("не владелец вещи и не арендатор"));
    }

    @Test
    void getAllUsersBookings_variousStates_success() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(booker.getId())).thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByIdAsc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));

        for (BookingState state : BookingState.values()) {
            List<ResponseBookingDto> result = bookingService.getAllUsersBookings(booker.getId(), state);
            assertFalse(result.isEmpty());
        }

        assertThrows(NullPointerException.class,
                () -> bookingService.getAllUsersBookings(booker.getId(), null));
    }

    @Test
    void getAllItemOwnerBookings_variousStates_success() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(owner.getId())).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));

        for (BookingState state : BookingState.values()) {
            if (state == BookingState.ALL || state == BookingState.CURRENT || state == BookingState.FUTURE ||
                    state == BookingState.PAST || state == BookingState.REJECTED || state == BookingState.WAITING) {
                List<ResponseBookingDto> result = bookingService.getAllItemOwnerBookings(owner.getId(), state);
                assertFalse(result.isEmpty());
            }
        }
    }

    @Test
    void isTimeCorrect_variousCases() {
        BookingDto dto1 = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().minusDays(1))
                .build();
        assertThrows(IncorrectBookingTimeException.class, () -> invokeIsTimeCorrect(dto1));

        BookingDto dto2 = BookingDto.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();
        assertThrows(IncorrectBookingTimeException.class, () -> invokeIsTimeCorrect(dto2));

        LocalDateTime now = LocalDateTime.now().plusDays(1);
        BookingDto dto3 = BookingDto.builder()
                .start(now)
                .end(now)
                .build();
        assertThrows(IncorrectBookingTimeException.class, () -> invokeIsTimeCorrect(dto3));

        BookingDto dto4 = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .build();
        assertThrows(IncorrectBookingTimeException.class, () -> invokeIsTimeCorrect(dto4));

        BookingDto dto5 = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        assertDoesNotThrow(() -> invokeIsTimeCorrect(dto5));
    }

    private void invokeIsTimeCorrect(BookingDto dto) {
        try {
            Method method = BookingServiceImpl.class.getDeclaredMethod("isTimeCorrect", BookingDto.class);
            method.setAccessible(true);
            method.invoke(bookingService, dto);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
