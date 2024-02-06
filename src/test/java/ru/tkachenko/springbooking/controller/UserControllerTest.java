package ru.tkachenko.springbooking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import ru.tkachenko.springbooking.AbstractTestController;
import ru.tkachenko.springbooking.StringTestUtils;
import ru.tkachenko.springbooking.dto.UpsertUserRequest;
import ru.tkachenko.springbooking.dto.UserResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest extends AbstractTestController {
    @Test
    @Order(1)
    public void whenFindAllUsers_thenReturnAllUsers() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<UserResponse> userResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(3, userResponses.size());
    }

    @Test
    public void whenFindByIdUser_thenReturnUser() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponse userResponse = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(1, userResponse.getId());
    }

    @Test
    public void whenFindByWrongIdUser_thenReturnError() throws Exception {
        var response = mockMvc.perform(get("/api/user/500"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_user_id_500_not_found_response.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateUser_thenReturnNewUser() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("New Name");
        request.setEmail("New Email");
        request.setPassword("NewPassword");

        String actualResponse = mockMvc.perform(post("/api/user")
                        .param("roleType", "ROLE_USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponse response = objectMapper.readValue(actualResponse, UserResponse.class);
        assertEquals(4, response.getId());
    }

    @Test
    public void whenCreateUserWithWrongEmail_thenReturnError() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("New Name");
        request.setEmail("Mail_1");
        request.setPassword("NewPassword");

        var response = mockMvc.perform(post("/api/user")
                        .param("roleType", "ROLE_USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_user_with_wrong_data_email.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateUserWithWrongName_thenReturnError() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("User_Name_1");
        request.setEmail("New Email");
        request.setPassword("NewPassword");

        var response = mockMvc.perform(post("/api/user")
                        .param("roleType", "ROLE_USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_user_with_wrong_data_name.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateUserWithoutName_thenReturnError() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setEmail("New Email");
        request.setPassword("NewPassword");

        var response = mockMvc.perform(post("/api/user")
                        .param("roleType", "ROLE_USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_user_without_data_name.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateUserWithoutEmail_thenReturnError() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("New Name");
        request.setPassword("NewPassword");

        var response = mockMvc.perform(post("/api/user")
                        .param("roleType", "ROLE_USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_user_without_data_email.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateUserWithoutPassword_thenReturnError() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("New Name");
        request.setEmail("New Email");

        var response = mockMvc.perform(post("/api/user")
                        .param("roleType", "ROLE_USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_user_without_data_password.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateUserWithoutRole_thenReturnError() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("New Name");
        request.setEmail("New Email");
        request.setPassword("NewPassword");

        var response = mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_user_without_data_role.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateUserWithWrongRole_thenReturnError() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("New Name User");
        request.setEmail("New Email User");
        request.setPassword("NewPassword");

        var response = mockMvc.perform(post("/api/user")
                        .param("roleType", "ROLE_USE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_user_with_wrong_data_role.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("New Name update");
        request.setEmail("New Email update");
        request.setPassword("NewPassword");

        String actualResponse = mockMvc.perform(put("/api/user/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponse response = objectMapper.readValue(actualResponse, UserResponse.class);
        assertEquals(2, response.getId());
        assertEquals("New Name update", response.getName());
        assertEquals("New Email update", response.getEmail());
    }

    @Test
    public void whenUpdateUserUseHimselfName_thenReturnUpdatedUser() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("User_Name_3");
        request.setEmail("Mail_3 update");
        request.setPassword("NewPassword");

        String actualResponse = mockMvc.perform(put("/api/user/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponse response = objectMapper.readValue(actualResponse, UserResponse.class);
        assertEquals(3, response.getId());
        assertEquals("User_Name_3", response.getName());
        assertEquals("Mail_3 update", response.getEmail());
    }

    @Test
    public void whenUpdateUserUseHimselfEmail_thenReturnUpdatedUser() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("User_Name_1 Update");
        request.setEmail("Mail_1");
        request.setPassword("NewPassword");

        String actualResponse = mockMvc.perform(put("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponse response = objectMapper.readValue(actualResponse, UserResponse.class);
        assertEquals(1, response.getId());
        assertEquals("User_Name_1 Update", response.getName());
        assertEquals("Mail_1", response.getEmail());
    }

    @Test
    public void whenDeleteUser_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(get("/api/user/3"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/user/3"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/user/3"))
                .andExpect(status().isNotFound());
    }
}
