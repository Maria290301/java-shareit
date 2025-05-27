package ru.practicum.shareit.itemRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(Long requesterId);

    @Query("SELECT r FROM ItemRequest r WHERE r.requester.id <> :userId ORDER BY r.created DESC")
    List<ItemRequest> findAllByOtherUsers(@Param("userId") Long userId, Pageable pageable);
}
