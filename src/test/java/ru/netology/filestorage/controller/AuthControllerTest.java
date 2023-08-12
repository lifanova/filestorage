package ru.netology.filestorage.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.netology.filestorage.exception.UnauthorizedError;
import ru.netology.filestorage.model.dto.AuthRequest;
import ru.netology.filestorage.model.dto.AuthResponse;
import ru.netology.filestorage.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void failedLogin() throws Exception {
        when(authService.login(any(AuthRequest.class))).thenThrow(UnauthorizedError.class);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"user\", \"password\": \"pass\"}")).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect((ResultMatcher) jsonPath("$.auth-token").doesNotExist());
    }

    @Test
    void successfulLogin() throws Exception {
        when(authService.login(any(AuthRequest.class))).thenReturn(new AuthResponse("thisisyourjwt"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"user1\", \"password\": \"12345\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.auth-token").value("thisisyourjwt"));
    }

    @Test
    @WithMockUser
    void logout() throws Exception {
        when(authService.logout(any(HttpServletRequest.class), any(HttpServletResponse.class), anyString())).thenReturn("true");

        mockMvc.perform(post("/logout")
                        .header("auth-token", "Bearer jwt"))
                .andDo(print())
                .andExpect(status().isOk());

    }


}
