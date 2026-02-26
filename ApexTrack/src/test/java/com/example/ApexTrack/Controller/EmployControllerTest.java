package com.example.ApexTrack.Controller;

import com.example.ApexTrack.Model.Employ;
import com.example.ApexTrack.Service.EmployService;
import com.example.ApexTrack.Service.JWTService;
import com.example.ApexTrack.Service.PasswordRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployController.class)
@AutoConfigureMockMvc(addFilters = false)   // disable security filters for controller tests
class EmployControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployService employService;

    @MockitoBean
    private JWTService jwtService;   // needed because JwtFilter is not loaded but still required as a bean

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister_Success() throws Exception {
        Employ employ = new Employ();
        employ.setUsername("john");
        employ.setEmail("john@example.com");
        employ.setPassword("secret");
        employ.setDepartment("IT");

        when(employService.createEmploy(any(Employ.class))).thenReturn("Created");

        mockMvc.perform(post("/api/employ/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employ)))
                .andExpect(status().isOk())
                .andExpect(content().string("Created"));
    }

    @Test
    void testRegister_AlreadyExists() throws Exception {
        Employ employ = new Employ();
        employ.setUsername("john");
        employ.setEmail("john@example.com");
        employ.setPassword("secret");
        employ.setDepartment("IT");

        when(employService.createEmploy(any(Employ.class))).thenReturn("Already exist");

        mockMvc.perform(post("/api/employ/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employ)))
                .andExpect(status().isOk())
                .andExpect(content().string("Already exist"));
    }

    @Test
    void testLogin_Success() throws Exception {
        PasswordRequest request = new PasswordRequest();
        request.setUsername("john");
        request.setPassword("pass");

        when(employService.verify(any(PasswordRequest.class))).thenReturn("jwt-token");

        mockMvc.perform(post("/api/employ/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));
    }

    @Test
    void testGetAllEmploy() throws Exception {
        when(employService.getAllEmploy()).thenReturn(List.of(new Employ()));

        mockMvc.perform(get("/api/employ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetEmployById_Found() throws Exception {
        Employ employ = new Employ();
        employ.setId(1L);
        when(employService.getEmployById(1L)).thenReturn(employ);

        mockMvc.perform(get("/api/employ/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetEmployById_NotFound() throws Exception {
        when(employService.getEmployById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/employ/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        PasswordRequest request = new PasswordRequest();
        request.setPassword("correct");

        when(employService.DeleteEmployById(1L, "correct")).thenReturn(true);

        mockMvc.perform(delete("/api/employ/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        PasswordRequest request = new PasswordRequest();
        request.setPassword("wrong");

        when(employService.DeleteEmployById(99L, "wrong")).thenReturn(false);

        mockMvc.perform(delete("/api/employ/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }
}