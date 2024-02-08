package ru.tkachenko.springbooking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.tkachenko.springbooking.AbstractTestController;
import ru.tkachenko.springbooking.StringTestUtils;
import ru.tkachenko.springbooking.dto.*;
import ru.tkachenko.springbooking.repository.UnavailableDateRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingControllerRTest extends AbstractTestController {

    @Autowired
    UnavailableDateRepository unavailableDateRepository;

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @Order(1)
    public void whenFindAllBookings_thenReturnAllBookings() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/booking"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<BookingResponse> bookingResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(4, bookingResponses.size());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenFindAllBookingsWithWrongRole_thenReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/booking"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenFindAllBookingsWithoutRole_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/booking"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenCreateBookingUserAuthorization_thenReturnNewBooking() throws Exception {
        UpsertBookingRequest request = new UpsertBookingRequest();
        request.setRoomId(2L);
        request.setArrivalDate(LocalDate.now().plusDays(1));
        request.setDepartureDate(LocalDate.now().plusDays(10));

        String actualResponse = mockMvc.perform(post("/api/booking")
                        .header("Authorization", USER_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long count = unavailableDateRepository.count();

        BookingResponse response = objectMapper.readValue(actualResponse, BookingResponse.class);
        assertEquals(5, response.getId());
        assertEquals(1, response.getUser().getId());
        assertEquals(32, count);
    }

    @Test
    public void whenCreateBookingWithoutRole_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/booking"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenCreateBookingWithoutArrivalDate_thenReturnError() throws Exception {
        UpsertBookingRequest request = new UpsertBookingRequest();
        request.setRoomId(2L);
        request.setDepartureDate(LocalDate.now().plusDays(10));

        var response = mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_booking_without_arrival_date.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "User")
    public void whenCreateBookingWithWrongArrivalDate_thenReturnError() throws Exception {
        UpsertBookingRequest request = new UpsertBookingRequest();
        request.setRoomId(2L);
        request.setArrivalDate(LocalDate.now().minusDays(1));
        request.setDepartureDate(LocalDate.now().plusDays(10));

        var response = mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_booking_with_wrong_arrival_date.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "User")
    public void whenCreateBookingWithoutDepartureDate_thenReturnError() throws Exception {
        UpsertBookingRequest request = new UpsertBookingRequest();
        request.setRoomId(2L);
        request.setArrivalDate(LocalDate.now().plusDays(1));

        var response = mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_booking_without_departure_date.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "User")
    public void whenCreateBookingWithWrongDepartureDate_thenReturnError() throws Exception {
        UpsertBookingRequest request = new UpsertBookingRequest();
        request.setRoomId(2L);
        request.setArrivalDate(LocalDate.now().plusDays(1));
        request.setDepartureDate(LocalDate.now().minusDays(10));

        var response = mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_booking_with_wrong_departure_date.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "User")
    public void whenCreateBookingWithoutRoomId_thenReturnError() throws Exception {
        UpsertBookingRequest request = new UpsertBookingRequest();
        request.setArrivalDate(LocalDate.now().plusDays(1));
        request.setDepartureDate(LocalDate.now().plusDays(10));

        var response = mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_booking_without_room_id.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateBookingWithWrongRoomId_thenReturnError() throws Exception {
        UpsertBookingRequest request = new UpsertBookingRequest();
        request.setRoomId(500L);
        request.setArrivalDate(LocalDate.now().plusDays(1));
        request.setDepartureDate(LocalDate.now().plusDays(10));

        var response = mockMvc.perform(post("/api/booking")
                        .header("Authorization", USER_AUTHORIZATION)
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
    public void whenCreateBookingWenArrivalDateAfterDepartureDate_thenReturnError() throws Exception {
        UpsertBookingRequest request = new UpsertBookingRequest();
        request.setRoomId(2L);
        request.setArrivalDate(LocalDate.now().plusDays(35));
        request.setDepartureDate(LocalDate.now().plusDays(30));

        var response = mockMvc.perform(post("/api/booking")
                        .header("Authorization", USER_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/when_arrival_date_after_departure_date.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }


    @Test
    public void whenCreateBookingWithFalseDate_thenReturnError() throws Exception {
        UpsertBookingRequest request = new UpsertBookingRequest();
        request.setRoomId(1L);
        request.setArrivalDate(LocalDate.of(2024, 2, 22));
        request.setDepartureDate(LocalDate.of(2024, 2, 27));

        var response = mockMvc.perform(post("/api/booking")
                        .header("Authorization", USER_AUTHORIZATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_booking_with_wrong_data_date.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }
}
