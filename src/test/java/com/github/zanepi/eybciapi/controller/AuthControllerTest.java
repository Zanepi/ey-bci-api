package com.github.zanepi.eybciapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zanepi.eybciapi.dto.LoginCredentials;
import com.github.zanepi.eybciapi.dto.LoginResponse;
import com.github.zanepi.eybciapi.dto.UserDto;
import com.github.zanepi.eybciapi.entity.User;
import com.github.zanepi.eybciapi.repository.UserRepository;
import com.github.zanepi.eybciapi.service.definition.IAuthService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private UserController userController;
    @Mock
    private IAuthService authService;
    @Mock
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    UserDto fullyBuiltUserDto;
    User fullyBuiltUser;

    final String badPassword = "this_is_a_bad_password";

    void setupRegister(){
        fullyBuiltUserDto = UserDto
                .builder()
                .name("Bobby Tables")
                .email("bobby@tables.io")
                .password("PasswordForEYTests123")
                .created(new Date())
                .modified(new Date())
                .last_login(new Date())
                .token("Token")
                .user_id(UUID.randomUUID())
                .phones(new ArrayList<>())
                .build();

    }

    void setupLogin(){
        fullyBuiltUser = User
                .builder()
                .name("Bobby Tables")
                .email("bobby@tables.io")
                .password(passwordEncoder.encode("PasswordForEYTests123"))
                .created(new Date())
                .modified(new Date())
                .last_login(new Date())
                .token("Token")
                .user_id(UUID.randomUUID())
                .build();

//        userRepository.save(fullyBuiltUser);
    }


    @Test
    void register() throws Exception {
        setupRegister();

        UserDto userDto = UserDto
                .builder()
                .name(fullyBuiltUserDto.getName())
                .email(fullyBuiltUserDto.getEmail())
                .password(fullyBuiltUserDto.getPassword())
                .build();

        String userDtoAsJson = objectMapper.writeValueAsString(userDto);

        when(authService.register(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userDtoAsJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void register_WhenUserHasNoEmail() throws Exception {
        setupRegister();

        UserDto userDto = UserDto
                .builder()
                .name(fullyBuiltUserDto.getName())
                .password(fullyBuiltUserDto.getPassword())
                .build();

        String userDtoAsJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userDtoAsJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void register_WhenUserHasNonConformingPassword() throws Exception {

        setupRegister();

        UserDto userDto = UserDto
                .builder()
                .name(fullyBuiltUserDto.getName())
                .email(fullyBuiltUserDto.getEmail())
                .password(badPassword)
                .build();

        String userDtoAsJson = objectMapper.writeValueAsString(userDto);

        when(authService.register(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userDtoAsJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Sql( scripts = {"classpath:insertMockUser.sql"})
    void login() throws Exception {

        setupLogin();

        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail(fullyBuiltUser.getEmail());
        credentials.setPassword("PasswordForEYTests123");

        String credentialsAsJson = objectMapper.writeValueAsString(credentials);

        when(authService.login(any(LoginCredentials.class))).thenReturn(new LoginResponse(credentials.getEmail(),"token"));

        User user = User
                .builder()
                .name(fullyBuiltUser.getName())
                .email(fullyBuiltUser.getEmail())
                .password(fullyBuiltUser.getPassword())
                .build();

        when(userRepository.findByEmail(credentials.getEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentialsAsJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @Sql( scripts = {"classpath:insertMockUser.sql"})
    void login_withWrongPassword() throws Exception {

        setupLogin();

        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail(fullyBuiltUser.getEmail());
        credentials.setPassword("PasswordForEYTests369");

        String credentialsAsJson = objectMapper.writeValueAsString(credentials);

        when(authService.login(any(LoginCredentials.class))).thenReturn(new LoginResponse(credentials.getEmail(),"token"));

        User user = User
                .builder()
                .name(fullyBuiltUser.getName())
                .email(fullyBuiltUser.getEmail())
                .password(fullyBuiltUser.getPassword())
                .build();

        when(userRepository.findByEmail(credentials.getEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentialsAsJson))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }
}