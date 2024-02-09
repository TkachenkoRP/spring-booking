package ru.tkachenko.springbooking.controller;

import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import ru.tkachenko.springbooking.AbstractTestController;
import ru.tkachenko.springbooking.StringTestUtils;
import ru.tkachenko.springbooking.dto.UpsertUserRequest;
import ru.tkachenko.springbooking.dto.UserRegisteredEvent;
import ru.tkachenko.springbooking.dto.UserResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest extends AbstractTestController {
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

        UserRegisteredEvent receivedEvent = getKafkaMessage(UserRegisteredEvent.class, KAFKA_USER_TOPIC);

        assertEquals(response.getId(), receivedEvent.getUserId());
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
}
