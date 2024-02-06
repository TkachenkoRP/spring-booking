package ru.tkachenko.springbooking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.tkachenko.springbooking.AbstractTestController;
import ru.tkachenko.springbooking.StringTestUtils;
import ru.tkachenko.springbooking.dto.HotelListResponse;
import ru.tkachenko.springbooking.dto.HotelResponse;
import ru.tkachenko.springbooking.dto.UpsertHotelRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HotelControllerTest extends AbstractTestController {
    @Test
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotels_thenReturnAllHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(5, hotelListResponse.getHotels().size());
    }

    @Test
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotelsWithPagination_thenReturnAllHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel?pageSize=2&pageNumber=0"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(2, hotelListResponse.getHotels().size());
    }

    @Test
    public void whenFindAllHotelsWithoutAuthenticated_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/hotel"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "User")
    @Order(2)
    public void whenFindByIdHotel_thenReturnHotel() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelResponse response = objectMapper.readValue(actualResponse, HotelResponse.class);

        assertEquals(1, response.getId());
        assertEquals("Hotel_1", response.getName());
        assertEquals("Hotel_Title_1", response.getTitle());
        assertEquals("Hotel_City_1", response.getCity());
        assertEquals("Hotel_Address_1", response.getAddress());
        assertEquals(0.555, response.getDistanceFromCityCenter());
        assertEquals(4.55, response.getRating());
        assertEquals(33, response.getNumberOfRatings());
    }

    @Test
    public void whenFindByIdHotelWithoutAuthenticated_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/hotel/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenFindByWrongId_thenReturnError() throws Exception {
        var response = mockMvc.perform(get("/api/hotel/500"))
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
    @Order(3)
    public void whenCreateHotel_thenReturnNewHotel() throws Exception {
        UpsertHotelRequest request = new UpsertHotelRequest();
        request.setName("New name");
        request.setTitle("New Title");
        request.setCity("New City");
        request.setAddress("New Address");
        request.setDistanceFromCityCenter(0.222);

        String actualResponse = mockMvc.perform(post("/api/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelResponse response = objectMapper.readValue(actualResponse, HotelResponse.class);
        assertEquals(6, response.getId());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenCreateHotelWithWrongRole_thenReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/hotel"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenCreateHotelWithoutRole_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/hotel"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateHotelWithoutName_thenReturnError() throws Exception {
        UpsertHotelRequest request = new UpsertHotelRequest();
        request.setTitle("New Title");
        request.setCity("New City");
        request.setAddress("New Address");
        request.setDistanceFromCityCenter(0.222);

        var response = mockMvc.perform(post("/api/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_hotel_without_data_name.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateHotelWithoutTitle_thenReturnError() throws Exception {
        UpsertHotelRequest request = new UpsertHotelRequest();
        request.setName("New name");
        request.setCity("New City");
        request.setAddress("New Address");
        request.setDistanceFromCityCenter(0.222);

        var response = mockMvc.perform(post("/api/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_hotel_without_data_title.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateHotelWithoutCity_thenReturnError() throws Exception {
        UpsertHotelRequest request = new UpsertHotelRequest();
        request.setName("New name");
        request.setTitle("New Title");
        request.setAddress("New Address");
        request.setDistanceFromCityCenter(0.222);

        var response = mockMvc.perform(post("/api/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_hotel_without_data_city.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateHotelWithoutAddress_thenReturnError() throws Exception {
        UpsertHotelRequest request = new UpsertHotelRequest();
        request.setName("New name");
        request.setTitle("New Title");
        request.setCity("New City");
        request.setDistanceFromCityCenter(0.222);

        var response = mockMvc.perform(post("/api/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_hotel_without_data_address.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateHotelWithoutDistance_thenReturnError() throws Exception {
        UpsertHotelRequest request = new UpsertHotelRequest();
        request.setName("New name");
        request.setTitle("New Title");
        request.setCity("New City");
        request.setAddress("New Address");

        var response = mockMvc.perform(post("/api/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_hotel_without_or_wrong_data_distance.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateHotelWithWrongDistance_thenReturnError() throws Exception {
        UpsertHotelRequest request = new UpsertHotelRequest();
        request.setName("New name");
        request.setTitle("New Title");
        request.setCity("New City");
        request.setAddress("New Address");
        request.setDistanceFromCityCenter(-0.222);

        var response = mockMvc.perform(post("/api/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_hotel_without_or_wrong_data_distance.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @Order(4)
    public void whenUpdateHotel_thenReturnUpdatedHotel() throws Exception {
        UpsertHotelRequest request = new UpsertHotelRequest();
        request.setName("New name");
        request.setTitle("New Title");
        request.setCity("New City");
        request.setAddress("New Address");
        request.setDistanceFromCityCenter(0.222);

        String actualResponse = mockMvc.perform(put("/api/hotel/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelResponse response = objectMapper.readValue(actualResponse, HotelResponse.class);
        assertEquals(5, response.getId());
        assertEquals("New name", response.getName());
        assertEquals("New Title", response.getTitle());
        assertEquals("New City", response.getCity());
        assertEquals("New Address", response.getAddress());
        assertEquals(0.222, response.getDistanceFromCityCenter());
        assertEquals(2.11, response.getRating());
        assertEquals(133, response.getNumberOfRatings());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenUpdateHotelWithWrongRole_thenReturnForbidden() throws Exception {
        mockMvc.perform(put("/api/hotel/5"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenUpdateHotelWithoutRole_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(put("/api/hotel/5"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenUpdateHotelWithWrongId_thenReturnError() throws Exception {
        UpsertHotelRequest request = new UpsertHotelRequest();
        request.setName("New name");
        request.setTitle("New Title");
        request.setCity("New City");
        request.setAddress("New Address");
        request.setDistanceFromCityCenter(0.222);

        var response = mockMvc.perform(put("/api/hotel/500")
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
    @Order(5)
    public void whenDeleteHotelById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(get("/api/hotel/5"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/hotel/5"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/hotel/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    public void whenDeleteHotelByIdWithWrongRole_thenReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/hotel/5"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenDeleteHotelByIdWithoutRole_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/hotel/5"))
                .andExpect(status().isUnauthorized());
    }
}
