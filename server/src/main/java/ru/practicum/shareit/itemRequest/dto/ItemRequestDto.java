package ru.practicum.shareit.itemRequest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank
    private String description;
    private LocalDateTime created;
    private List<ItemResponseDto> items = new ArrayList<>();
}
