package ru.practicum.item;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.BookingStatus;
import ru.practicum.booking.ShortBookingDto;
import ru.practicum.exception.IncorrectUserException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + ownerId + " не найден"));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        Item existedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id=" + itemId + " не найдена"));
        if (!existedItem.getOwner().getId().equals(ownerId)) {
            throw new IncorrectUserException("Неверный владелец");
        }
        if (itemDto.getName() != null) existedItem.setName(itemDto.getName());
        if (itemDto.getDescription() != null) existedItem.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) existedItem.setAvailable(itemDto.getAvailable());

        return ItemMapper.toItemDto(itemRepository.save(existedItem));
    }

    @Override
    @Transactional
    public ItemDtoBookingsAndComments getItemById(Long ownerId, Long itemId) {
        Item existedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id=" + itemId + " не найдена"));

        List<Booking> itemBookings = bookingRepository
                .findAllByItemIdAndStatusNotOrderByStartAsc(existedItem.getId(), BookingStatus.REJECTED);

        List<Booking> bookingsBefore = itemBookings.stream()
                .filter(i -> i.getStart().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        List<Booking> bookingsAfter = itemBookings.stream()
                .filter(i -> i.getStart().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        List<CommentDto> commentDtoList = CommentMapper.listToCommentDto(
                commentRepository.findAllByItemIdOrderByCreatedAsc(itemId)
        );

        ShortBookingDto lastBooking = bookingsBefore.isEmpty() ? null : new ShortBookingDto(
                bookingsBefore.get(bookingsBefore.size() - 1).getId(),
                bookingsBefore.get(bookingsBefore.size() - 1).getBooker().getId()
        );

        ShortBookingDto nextBooking = bookingsAfter.isEmpty() ? null : new ShortBookingDto(
                bookingsAfter.get(0).getId(),
                bookingsAfter.get(0).getBooker().getId()
        );

        ItemDtoBookingsAndComments dto = new ItemDtoBookingsAndComments();
        dto.setId(existedItem.getId());
        dto.setName(existedItem.getName());
        dto.setDescription(existedItem.getDescription());
        dto.setAvailable(existedItem.isAvailable());
        dto.setComments(commentDtoList);

        if (existedItem.getOwner().getId().equals(ownerId)) {
            dto.setLastBooking(lastBooking);
            dto.setNextBooking(nextBooking);
        }
        return dto;
    }

    @Override
    public List<ItemDtoBookingsAndComments> getUserItems(Long ownerId) {
        List<Item> userItems = itemRepository.findByOwnerId(ownerId);
        return userItems.stream().map(item -> {
                    List<Booking> itemBookings = bookingRepository
                            .findAllByItemIdAndStatusNotOrderByStartAsc(item.getId(), BookingStatus.REJECTED);

                    List<Booking> bookingsBefore = itemBookings.stream()
                            .filter(i -> i.getEnd().isBefore(LocalDateTime.now()))
                            .collect(Collectors.toList());
                    List<Booking> bookingsAfter = itemBookings.stream()
                            .filter(i -> i.getStart().isAfter(LocalDateTime.now()))
                            .collect(Collectors.toList());

                    ShortBookingDto lastBooking = bookingsBefore.isEmpty() ? null : new ShortBookingDto(
                            bookingsBefore.get(bookingsBefore.size() - 1).getId(),
                            bookingsBefore.get(bookingsBefore.size() - 1).getBooker().getId()
                    );

                    ShortBookingDto nextBooking = bookingsAfter.isEmpty() ? null : new ShortBookingDto(
                            bookingsAfter.get(0).getId(),
                            bookingsAfter.get(0).getBooker().getId()
                    );

                    List<CommentDto> commentDtoList = CommentMapper.listToCommentDto(
                            commentRepository.findAllByItemIdOrderByCreatedAsc(item.getId())
                    );

                    ItemDtoBookingsAndComments dto = new ItemDtoBookingsAndComments();
                    dto.setId(item.getId());
                    dto.setName(item.getName());
                    dto.setDescription(item.getDescription());
                    dto.setAvailable(item.isAvailable());
                    dto.setLastBooking(lastBooking);
                    dto.setNextBooking(nextBooking);
                    dto.setComments(commentDtoList);

                    return dto;
                }).sorted(Comparator.comparing(ItemDtoBookingsAndComments::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> searchedItems = itemRepository.searchAvailableItems(text);
        return ItemMapper.listToItemDto(searchedItems);
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long itemId, Long authorId) {
        if (commentDto.getText().isBlank()) {
            throw new ValidationException("Отсутствует текст в комментарии");
        }
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + authorId + " не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id=" + itemId + " не найдена"));

        List<Booking> bookingList = bookingRepository
                .findAllByItemIdAndBookerIdAndEndBefore(itemId, authorId, LocalDateTime.now());

        if (bookingList.isEmpty()) {
            throw new ValidationException("Пользователь не брал вещь в аренду или срок аренды еще не закончился");
        }

        Comment comment = CommentMapper.toComment(commentDto, item, user);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }
}