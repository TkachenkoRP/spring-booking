package ru.tkachenko.springbooking.controller;

import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.tkachenko.springbooking.AbstractTestController;
import ru.tkachenko.springbooking.StringTestUtils;
import ru.tkachenko.springbooking.dto.RoomResponse;
import ru.tkachenko.springbooking.dto.UpsertRoomRequest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoomControllerTest extends AbstractTestController {

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
