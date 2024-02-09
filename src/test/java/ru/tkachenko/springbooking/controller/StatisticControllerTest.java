package ru.tkachenko.springbooking.controller;

import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import ru.tkachenko.springbooking.AbstractTestController;
import ru.tkachenko.springbooking.StringTestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StatisticControllerTest extends AbstractTestController {
    @Test
    public void whenCalledWithoutRole_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "User")
    public void whenCalledWithWrongRole_thenReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCalledWithoutPath_thenReturnError() throws Exception {
        var response = mockMvc.perform(get("/api/stats"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/called_stats_without_data_path.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }
}
