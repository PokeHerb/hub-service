package org.pokeherb.hubservice.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HubApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName(value = "허브 삭제 시 해당 허브가 포함된 허브 루트도 함께 삭제")
    @WithMockUser(username = "testUser", roles = {"MASTER"})
    void deleteHubTest() throws Exception {
        mockMvc.perform(delete("/v1/hub/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName(value = "허브 삭제 시 마스터 관리자가 아닐 경우 접근 권한 없음")
    @WithMockUser(username = "testUser", roles = {"HUB_MANAGER"})
    void deleteHubAuthorizationTest() throws Exception {
        mockMvc.perform(delete("/v1/hub/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
