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
import ru.tkachenko.springbooking.dto.HotelListResponse;
import ru.tkachenko.springbooking.dto.HotelResponse;
import ru.tkachenko.springbooking.dto.UpsertHotelRequest;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotelsWithIdFilter_thenReturnFilteredHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(1, hotelListResponse.getHotels().size());
        assertEquals(1, hotelListResponse.getHotels().get(0).getId());
    }

    @Test
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotelsWithWrongIdFilter_thenReturnFilteredHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel")
                        .param("id", "0"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(0, hotelListResponse.getHotels().size());
    }

    @Test
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotelsWithNameFilter_thenReturnFilteredHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel")
                        .param("name", "Hotel_3"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(1, hotelListResponse.getHotels().size());
        assertEquals(3, hotelListResponse.getHotels().get(0).getId());
    }

    @Test
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotelsWithTitleFilter_thenReturnFilteredHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel")
                        .param("title", "Hotel_Title_2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        List<HotelResponse> hotels = hotelListResponse.getHotels();

        assertEquals(1, hotels.size());

        for (HotelResponse hotel : hotels) {
            assertEquals("Hotel_Title_2", hotel.getTitle());
        }
    }

    @Test
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotelsWithCityFilter_thenReturnFilteredHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel")
                        .param("city", "Hotel_City_2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        List<HotelResponse> hotels = hotelListResponse.getHotels();

        assertEquals(2, hotels.size());

        for (HotelResponse hotel : hotels) {
            assertEquals("Hotel_City_2", hotel.getCity());
        }
    }

    @Test
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotelsWithAddressFilter_thenReturnFilteredHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel")
                        .param("address", "Hotel_Address_4"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        List<HotelResponse> hotels = hotelListResponse.getHotels();

        assertEquals(1, hotels.size());

        for (HotelResponse hotel : hotels) {
            assertEquals("Hotel_Address_4", hotel.getAddress());
        }
    }

    @Test
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotelsWithDistanceFilter_thenReturnFilteredHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel")
                        .param("distance", "5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        List<HotelResponse> hotels = hotelListResponse.getHotels();

        assertEquals(3, hotels.size());

        for (HotelResponse hotel : hotels) {
            assertTrue(hotel.getDistanceFromCityCenter() <= 5.0);
        }
    }

    @Test
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotelsWithRatingFilter_thenReturnFilteredHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel")
                        .param("rating", "4"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        List<HotelResponse> hotels = hotelListResponse.getHotels();

        assertEquals(2, hotels.size());

        for (HotelResponse hotel : hotels) {
            assertTrue(hotel.getRating() >= 4.0);
        }
    }

    @Test
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotelsWithNumberOfRatingsFilter_thenReturnFilteredHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel")
                        .param("numberOfRatings", "100"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        List<HotelResponse> hotels = hotelListResponse.getHotels();

        assertEquals(3, hotels.size());

        for (HotelResponse hotel : hotels) {
            assertTrue(hotel.getNumberOfRatings() >= 100);
        }
    }

    @Test
    @WithMockUser(username = "User")
    @Order(1)
    public void whenFindAllHotelsWithRatingAndNumberOfRatingsFilter_thenReturnFilteredHotels() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/hotel")
                        .param("rating", "3.5")
                        .param("numberOfRatings", "100"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelListResponse hotelListResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        List<HotelResponse> hotels = hotelListResponse.getHotels();

        assertEquals(1, hotels.size());

        for (HotelResponse hotel : hotels) {
            assertTrue(hotel.getRating() >= 3.5);
            assertTrue(hotel.getNumberOfRatings() >= 100);
        }
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
        assertEquals(0.55, response.getDistanceFromCityCenter());
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

    @Test
    @WithMockUser(username = "User")
    public void whenChangeRating_thenReturnHotelWithNewRating() throws Exception {
        String actualResponse = mockMvc.perform(put("/api/hotel/4/vote/5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelResponse response = objectMapper.readValue(actualResponse, HotelResponse.class);

        assertEquals(4, response.getId());
        assertEquals("Hotel_4", response.getName());
        assertEquals("Hotel_Title_4", response.getTitle());
        assertEquals("Hotel_City_1", response.getCity());
        assertEquals("Hotel_Address_4", response.getAddress());
        assertEquals(1.15, response.getDistanceFromCityCenter());
        assertTrue(response.getRating() > 3.33);
        assertEquals(3.36, response.getRating());
        assertEquals(55, response.getNumberOfRatings());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenChangeRatingMax_thenReturnHotelWithOldRating() throws Exception {
        String actualResponse = mockMvc.perform(put("/api/hotel/2/vote/5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelResponse response = objectMapper.readValue(actualResponse, HotelResponse.class);

        assertEquals(2, response.getId());
        assertEquals("Hotel_2", response.getName());
        assertEquals("Hotel_Title_2", response.getTitle());
        assertEquals("Hotel_City_2", response.getCity());
        assertEquals("Hotel_Address_2", response.getAddress());
        assertEquals(12.12, response.getDistanceFromCityCenter());
        assertEquals(5.00, response.getRating());
        assertEquals(101, response.getNumberOfRatings());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenChangeRatingMin_thenReturnHotelWithOldRating() throws Exception {
        String actualResponse = mockMvc.perform(put("/api/hotel/3/vote/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        HotelResponse response = objectMapper.readValue(actualResponse, HotelResponse.class);

        assertEquals(3, response.getId());
        assertEquals("Hotel_3", response.getName());
        assertEquals("Hotel_Title_3", response.getTitle());
        assertEquals("Hotel_City_2", response.getCity());
        assertEquals("Hotel_Address_3", response.getAddress());
        assertEquals(3.45, response.getDistanceFromCityCenter());
        assertEquals(1.00, response.getRating());
        assertEquals(101, response.getNumberOfRatings());
    }

    @ParameterizedTest
    @WithMockUser(username = "User")
    @MethodSource("invalidNumbers")
    public void whenChangeRatingWithWrongNewMark_thenReturnUnauthorized(int newMark) throws Exception {
        var response = mockMvc.perform(put("/api/hotel/4/vote/" + newMark))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_hotel_new_mark.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenChangeRatingWithoutRole_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(put("/api/hotel/4/vote/5"))
                .andExpect(status().isUnauthorized());
    }

    private static Stream<Arguments> invalidNumbers() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(0),
                Arguments.of(6)
        );
    }
}
