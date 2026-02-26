package com.example.ApexTrack.Service;

import com.example.ApexTrack.Model.Employ;
import com.example.ApexTrack.repository.EmployRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployServiceTest {

    @Mock
    private EmployRepo employRepo;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private StatusCalculationService statusCalculationService;

    @InjectMocks
    private EmployService employService;

    private Employ testEmploy;
    private PasswordRequest loginRequest;

    @BeforeEach
    void setUp() {
        testEmploy = new Employ();
        testEmploy.setId(1L);
        testEmploy.setUsername("john");
        testEmploy.setEmail("john@example.com");
        testEmploy.setPassword("encodedPassword");
        testEmploy.setDepartment("IT");
        testEmploy.setRole("Role_USER");

        loginRequest = new PasswordRequest();
        loginRequest.setUsername("john");
        loginRequest.setPassword("rawPassword");
    }

    @Test
    void testGetAllEmploy() {
        when(employRepo.findAll()).thenReturn(List.of(testEmploy));
        List<Employ> result = employService.getAllEmploy();
        assertThat(result).hasSize(1).contains(testEmploy);
    }

    @Test
    void testGetEmployById_Found() {
        when(employRepo.findById(1L)).thenReturn(Optional.of(testEmploy));
        Employ result = employService.getEmployById(1L);
        assertThat(result).isEqualTo(testEmploy);
    }

    @Test
    void testGetEmployById_NotFound() {
        when(employRepo.findById(99L)).thenReturn(Optional.empty());
        Employ result = employService.getEmployById(99L);
        assertThat(result).isNull();
    }

    @Test
    void testCreateEmploy_Success() {
        when(employRepo.findByEmail(testEmploy.getEmail())).thenReturn(null);
        when(employRepo.save(any(Employ.class))).thenReturn(testEmploy);

        String result = employService.createEmploy(testEmploy);
        assertThat(result).isEqualTo("Created");
        verify(employRepo).save(any(Employ.class));
    }

    @Test
    void testCreateEmploy_AlreadyExists() {
        when(employRepo.findByEmail(testEmploy.getEmail())).thenReturn(new Employ());
        String result = employService.createEmploy(testEmploy);
        assertThat(result).isEqualTo("Already exist");
        verify(employRepo, never()).save(any());
    }

    @Test
    void testDeleteEmployById_Success() {
        when(employRepo.findById(1L)).thenReturn(Optional.of(testEmploy));
        // Use a real encoder to set a known password
        String rawPassword = "correct";
        String encoded = new BCryptPasswordEncoder(12).encode(rawPassword);
        testEmploy.setPassword(encoded);
        doNothing().when(employRepo).delete(testEmploy);

        boolean result = employService.DeleteEmployById(1L, rawPassword);
        assertThat(result).isTrue();
        verify(employRepo).delete(testEmploy);
    }

    @Test
    void testDeleteEmployById_WrongPassword() {
        when(employRepo.findById(1L)).thenReturn(Optional.of(testEmploy));
        // testEmploy has a dummy password, not matching "wrong"
        boolean result = employService.DeleteEmployById(1L, "wrong");
        assertThat(result).isFalse();
        verify(employRepo, never()).delete(any());
    }

    @Test
    void testDeleteEmployById_NotFound() {
        when(employRepo.findById(99L)).thenReturn(Optional.empty());
        boolean result = employService.DeleteEmployById(99L, "any");
        assertThat(result).isFalse();
        verify(employRepo, never()).delete(any());
    }

    @Test
    void testVerify_ValidCredentials() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(jwtService.generateToken("john")).thenReturn("jwt-token");

        String token = employService.verify(loginRequest);
        assertThat(token).isEqualTo("jwt-token");
    }

    @Test
    void testVerify_InvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        String result = employService.verify(loginRequest);
        assertThat(result).startsWith("Fail");
    }
}