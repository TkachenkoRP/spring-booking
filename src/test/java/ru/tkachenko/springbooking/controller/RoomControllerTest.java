package ru.tkachenko.springbooking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.tkachenko.springbooking.AbstractTestController;
import ru.tkachenko.springbooking.StringTestUtils;
import ru.tkachenko.springbooking.dto.RoomResponse;
import ru.tkachenko.springbooking.dto.UpsertRoomRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RoomControllerTest extends AbstractTestController {

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRooms_thenReturnAllRooms() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(20, roomResponses.size());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithPagination_thenReturnFilteredRooms() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("pageSize", "3")
                        .param("pageNumber", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(3, roomResponses.size());
        assertEquals(4, roomResponses.get(0).getId());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithIdFilter_thenReturnFilteredRooms() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(1, roomResponses.size());
        assertEquals(1, roomResponses.get(0).getId());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithNameFilter_thenReturnFilteredRooms() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("name", "Room_Name_3_1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(1, roomResponses.size());
        assertEquals(11, roomResponses.get(0).getId());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithMinPriceFilter_thenReturnFilteredRooms() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("minPrice", "2000"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        for (RoomResponse room : roomResponses) {
            assertTrue(room.getPrice() >= 2000);
        }
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithMaxPriceFilter_thenReturnFilteredRooms() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("maxPrice", "2000"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        for (RoomResponse room : roomResponses) {
            assertTrue(room.getPrice() <= 2000);
        }
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithMinMaxPriceFilter_thenReturnFilteredRooms() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("minPrice", "1000")
                        .param("maxPrice", "3000"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        for (RoomResponse room : roomResponses) {
            assertTrue(room.getPrice() >= 1000 && room.getPrice() <= 3000);
        }
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithCountGuestFilter_thenReturnFilteredRooms() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("countGuest", "3"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        for (RoomResponse room : roomResponses) {
            assertEquals((byte) 3, room.getCapacity());
        }
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithArrivalAndDepartureDateFilter_thenReturnAllRooms() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("arrivalDate", "2221-03-02")
                        .param("departureDate", "2221-03-04"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(20, roomResponses.size());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithArrivalAndDepartureDateFilter_thenReturnFilteredRooms() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("arrivalDate", "2221-02-22")
                        .param("departureDate", "2221-03-10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(17, roomResponses.size());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithArrivalAndDepartureDateFilter_thenReturnFilteredRoomsPlusOne() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("arrivalDate", "2221-02-22")
                        .param("departureDate", "2221-03-04"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(18, roomResponses.size());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithArrivalAndDepartureDateFilter_thenReturnFilteredRoomsPlusTwo() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("arrivalDate", "2221-02-01")
                        .param("departureDate", "2221-02-23"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(19, roomResponses.size());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithWrongDateFilter_thenReturnError() throws Exception {
        var response = mockMvc.perform(get("/api/room")
                        .param("arrivalDate", "2221-033-01")
                        .param("departureDate", "2221-03-04"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_date_room_filter.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithArrivalDateIsBeforeDepartureDateFilter_thenReturnError() throws Exception {
        var response = mockMvc.perform(get("/api/room")
                        .param("arrivalDate", "2221-03-05")
                        .param("departureDate", "2221-03-04"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/when_arrival_date_after_departure_date.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithDateIsBeforeNowFilter_thenReturnError() throws Exception {
        var response = mockMvc.perform(get("/api/room")
                        .param("arrivalDate", LocalDate.now().minusDays(1).toString())
                        .param("departureDate", "2221-03-04"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/when_date_before_now.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithArrivalDateFilter_thenReturnAllRoomsWithoutFilter() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("arrivalDate", "2221-02-22"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(20, roomResponses.size());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithDepartureDateFilter_thenReturnAllRoomsWithoutFilter() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("departureDate", "2221-03-07"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(20, roomResponses.size());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "User")
    public void whenFindAllRoomsWithHotelIdFilter_thenReturnFilteredRooms() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room")
                        .param("hotelId", "5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(4, roomResponses.size());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenFindByIdRoom_thenReturnRoom() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/room/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RoomResponse response = objectMapper.readValue(actualResponse, RoomResponse.class);

        assertEquals(1, response.getId());
        assertEquals("Room_Name_1_1", response.getName());
        assertEquals("Room_Description_1", response.getDescription());
        assertEquals(1, response.getNumber());
        assertEquals(4500, response.getPrice());
        assertEquals((byte) 3, response.getCapacity());
    }

    @Test
    public void whenFindByIdRoomWithoutRole_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/room/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenFindByWrongId_thenReturnError() throws Exception {
        var response = mockMvc.perform(get("/api/room/500"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_room_id_500_not_found_response.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateRoom_thenReturnNewRoom() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setNumber(55);
        request.setPrice(555.55);
        request.setCapacity((byte) 3);
        request.setHotelId(1L);

        String actualResponse = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RoomResponse response = objectMapper.readValue(actualResponse, RoomResponse.class);
        assertEquals(21, response.getId());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenCreateRoomWithWrongRole_thenReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/room"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenCreateRoomWithoutRole_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/room"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateRoomWithoutName_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setDescription("New Description");
        request.setNumber(55);
        request.setPrice(555.55);
        request.setCapacity((byte) 3);
        request.setHotelId(1L);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_without_data_name.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateRoomWithoutDescription_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setNumber(55);
        request.setPrice(555.55);
        request.setCapacity((byte) 3);
        request.setHotelId(1L);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_without_data_description.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateRoomWithoutNumber_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setPrice(555.55);
        request.setCapacity((byte) 3);
        request.setHotelId(1L);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_without_data_number.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }


    @ParameterizedTest
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @MethodSource("invalidNumbers")
    public void whenCreateRoomWithWrongNumber_thenReturnError(int number) throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setNumber(number);
        request.setPrice(555.55);
        request.setCapacity((byte) 3);
        request.setHotelId(1L);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_with_wrong_data_number.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateRoomWithoutPrice_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setNumber(55);
        request.setCapacity((byte) 3);
        request.setHotelId(1L);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_without_or_wrong_data_price.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateRoomWithWrongPrice_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setNumber(55);
        request.setPrice(-555.55);
        request.setCapacity((byte) 3);
        request.setHotelId(1L);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_without_or_wrong_data_price.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateRoomWithoutCapacity_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setNumber(55);
        request.setPrice(555.55);
        request.setHotelId(1L);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_without_or_wrong_data_capacity.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateRoomWitWrongCapacity_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setNumber(55);
        request.setPrice(555.55);
        request.setCapacity((byte) -3);
        request.setHotelId(1L);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_without_or_wrong_data_capacity.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateRoomWithoutHotelId_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setNumber(55);
        request.setPrice(555.55);
        request.setCapacity((byte) 3);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_without_data_hotel_id.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateRoomWithWrongHotelId_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setNumber(55);
        request.setPrice(555.55);
        request.setCapacity((byte) 3);
        request.setHotelId(500L);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_hotel_id_500_not_found_response.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenUpdateRoom_thenReturnUpdatedRoom() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setNumber(55);
        request.setPrice(555.55);
        request.setCapacity((byte) 3);
        request.setHotelId(1L);

        String actualResponse = mockMvc.perform(put("/api/room/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RoomResponse response = objectMapper.readValue(actualResponse, RoomResponse.class);
        assertEquals(2, response.getId());
        assertEquals("New Name", response.getName());
        assertEquals("New Description", response.getDescription());
        assertEquals(55, response.getNumber());
        assertEquals(555.55, response.getPrice());
        assertEquals((byte) 3, response.getCapacity());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenUpdateRoomWithWrongRole_thenReturnForbidden() throws Exception {
        mockMvc.perform(put("/api/room/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenUpdateRoomWithoutRole_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(put("/api/room/2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenUpdateRoomWithWrongId_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setNumber(55);
        request.setPrice(555.55);
        request.setCapacity((byte) 3);
        request.setHotelId(1L);

        var response = mockMvc.perform(put("/api/room/500")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_room_id_500_not_found_response.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenDeleteRoomById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(get("/api/room/20"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/room/20"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/room/20"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenDeleteRoomByIdWithWrongRole_thenReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/room/20"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenDeleteRoomByIdWithWrongRole_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/room/20"))
                .andExpect(status().isUnauthorized());
    }

    private static Stream<Arguments> invalidNumbers() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(0),
                Arguments.of(101)
        );
    }
}
