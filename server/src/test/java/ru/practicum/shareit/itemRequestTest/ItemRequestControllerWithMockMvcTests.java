package ru.practicum.shareit.itemRequestTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.itemRequest.ItemRequestController;
import ru.practicum.shareit.itemRequest.ItemRequestService;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerWithMockMvcTests {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void init() {
        itemRequestDto = ItemRequestDto
                .builder()
                .id(1L)
                .description("item request description")
                .build();
    }

    @Test
    void createTest() throws Exception {
        when(itemRequestService.create(anyLong(), any()))
                .thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
    }

    @Test
    void createWithEmptyDescriptionTest() throws Exception {
        ItemRequestDto emptyDescriptionDto = ItemRequestDto.builder().description("").build();

        ItemRequestDto returnedDto = ItemRequestDto.builder()
                .id(1L)
                .description("")
                .build();

        when(itemRequestService.create(anyLong(), any(ItemRequestDto.class))).thenReturn(returnedDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(emptyDescriptionDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(""));
    }

    @Test
    void getByIdNotFoundTest() throws Exception {
        when(itemRequestService.getById(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("ItemRequest not found"));

        mvc.perform(get("/requests/999")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("ItemRequest not found"));
    }

    @Test
    void getAllByUserTest() throws Exception {
        List<ItemRequestDto> requests = List.of(itemRequestDto);
        when(itemRequestService.getAllByUser(anyLong())).thenReturn(requests);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requests)));
    }

    @Test
    void getAllByUserNotFoundTest() throws Exception {
        when(itemRequestService.getAllByUser(anyLong())).thenReturn(Collections.emptyList());

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getByIdTest() throws Exception {
        when(itemRequestService.getById(anyLong(), anyLong())).thenReturn(itemRequestDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
    }

    @Test
    void createWithNonExistentUserTest() throws Exception {
        when(itemRequestService.create(anyLong(), any()))
                .thenThrow(new NotFoundException("User not found"));

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 999L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    void getAllByNonExistentUserTest() throws Exception {
        when(itemRequestService.getAllByUser(anyLong())).thenThrow(new NotFoundException("User not found"));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 999L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    void getAllTest() throws Exception {
        List<ItemRequestDto> requests = List.of(itemRequestDto);
        when(itemRequestService.getAll(0, 10, 1L)).thenReturn(requests);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requests)));
    }

    @Test
    void getAllWithInvalidPaginationTest() throws Exception {
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "-1")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "-5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
