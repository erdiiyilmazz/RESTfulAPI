package com.erdi.microservice.restful.rest.users;

import com.erdi.microservice.restful.rest.users.dtos.UserDTO;
import com.erdi.microservice.restful.rest.users.entities.User;
import com.erdi.microservice.restful.rest.users.services.UserService;
import com.erdi.microservice.restful.rest.users.services.UserTestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerIntegrationTest {

    final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void getUserPresentationList() throws Exception {
        UserDTO user1 = new UserDTO(UserTestHelper.getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788"));
        UserDTO user2 = new UserDTO(UserTestHelper.getUserTestData(2L, "tony", "Tony",
                "Soprano", "tony.test@gmail.com", "+9062661272233"));
        UserDTO user3 = new UserDTO(UserTestHelper.getUserTestData(3L, "johnny", "Johnny",
                "Cash", "franco.test@gmail.com", "+1666552334477"));

        List<UserDTO> userList = Arrays.asList(user1, user2, user3);

        given(userService.getUserPresentationList()).willReturn(userList);

        mvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userList[0].name").value("Erdi"))
                .andExpect(jsonPath("$.userList[0].surname").value("Yilmaz"))
                .andExpect(jsonPath("$.userList[0].contactDTO.email").value("erdiyilmaz@gmail.com"))
                .andExpect(jsonPath("$.userList[0].username").value("erdi"))
                .andExpect(jsonPath("$.userList[0].contactDTO.phone").value("+905059667788"))
                .andExpect(jsonPath("$.userList[1].name").value("Tony"))
                .andExpect(jsonPath("$.userList[1].surname").value("Soprano"))
                .andExpect(jsonPath("$.userList[1].contactDTO.email").value("tony.test@gmail.com"))
                .andExpect(jsonPath("$.userList[1].username").value("tony"))
                .andExpect(jsonPath("$.userList[1].contactDTO.phone").value("+9062661272233"))
                .andExpect(jsonPath("$.userList[2].name").value("Johnny"))
                .andExpect(jsonPath("$.userList[2].surname").value("Cash"))
                .andExpect(jsonPath("$.userList[2].contactDTO.email").value("johnny.test@gmail.com"))
                .andExpect(jsonPath("$.userList[2].username").value("johnny"))
                .andExpect(jsonPath("$.userList[2].contactDTO.phone").value("+1666552334477"));
    }

    @Test
    public void getUserById() throws Exception {
        User user1 = UserTestHelper.getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        user1.setBirthDate(LocalDate.of(1988, 11, 22));

        given(userService.getUserById(1L)).willReturn(user1);

        Long userId = 1L;

        mvc.perform(MockMvcRequestBuilders.get("/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name").value("Erdi"))
                .andExpect(jsonPath("surname").value("Yilmaz"))
                .andExpect(jsonPath("contactDTO.email").value("erdiyilmaz@gmail.com"))
                .andExpect(jsonPath("username").value("erdi"))
                .andExpect(jsonPath("birthDate").value("1988-11-22"))
                .andExpect(jsonPath("contactDTO.phone").value("+905059667788"));
    }

}
