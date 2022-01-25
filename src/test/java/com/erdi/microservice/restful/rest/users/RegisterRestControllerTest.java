package com.erdi.microservice.restful.rest.users;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.erdi.microservice.restful.rest.users.dtos.UserDTO;
import com.erdi.microservice.restful.rest.users.dtos.requests.RegisterUserAccountDTO;
import com.erdi.microservice.restful.rest.users.services.UserService;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterRestControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserService userService;

    @Test
    public void test_createNewUserAccount() {
        // create a new user using the quick account endpoint
        RegisterUserAccountDTO quickAccount = RegisterUserAccountDTO.builder()
                .username("rosa")
                .password("RosaQWERTY")
                .name("Rosa")
                .surname("Luxemburg")
                .gender("FEMALE")
                .email("rosa.luxemburg@gmail.com")
                .build();

        String userQuickAccountURL = "/users/register";

        HttpEntity<RegisterUserAccountDTO> request = new HttpEntity<>(quickAccount);
        ResponseEntity<UserDTO> response = restTemplate.postForEntity(userQuickAccountURL, request, UserDTO.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        UserDTO userDTO = response.getBody();

        assertNotNull(userDTO);
        assertEquals("rosa", userDTO.getUsername());
        assertEquals("Rosa", userDTO.getName());
        assertEquals("Luxemburg", userDTO.getSurname());
        assertEquals("FEMALE", userDTO.getGender());

        assertNotNull(userDTO.getContactDTO());
        assertEquals("rosa.luxemburg@gmail.com", userDTO.getContactDTO().getEmail());

        // delete the created user
        userService.deleteUserById(userDTO.getId());
    }

}
