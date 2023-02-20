package com.csaba79coder.userlogin;

import com.csaba79coder.models.UserModel;
import com.csaba79coder.models.UserRegistrationModel;
import com.csaba79coder.userlogin.entity.User;
import com.csaba79coder.userlogin.persistence.UserRepository;
import com.csaba79coder.userlogin.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("renderAllUsersWithNonEmptyResult")
    void renderAllUsersWithNonEmptyResult() throws Exception {
        // Given
        saveDummyUser();

        // When
        ResultActions response = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then
        then(response)
                .isNotNull();

        then(response)
                .usingRecursiveComparison()
                .isEqualTo(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("RenderAllUsersWithEmptyResult")
    void renderAllUsersWithEmptyResult() throws Exception {
        // Given

        // When
        ResultActions response = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then
        then(response)
                .usingRecursiveComparison()
                .isEqualTo(jsonPath("$.size()").value(0));
    }
    
    @Test
    @DisplayName("Add new user to database")
    void addNewUserToDatabase() throws Exception {
        // Given
        UserRegistrationModel model = new UserRegistrationModel()
                .email("csabavadasz79@gmail.com")
                .password("Geeks@portal20")
                .repeatedPassword("Geeks@portal20");
        UserModel expectedModel = new UserModel()
                .email("csabavadasz79@gmail.com");

        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(model))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

        // Then
        then(response)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedModel);
    }

    private void saveDummyUser() {
        User dummyUser = new User();
        dummyUser.setId(UUID.fromString("6511513f-ffdd-435b-84b0-2f42bfd9ca7e"));
        dummyUser.setEmail("csabavadasz79@gmail.com");
        dummyUser.setPassword("Geeks@portal20");
        userRepository.save(dummyUser);
    }
}
