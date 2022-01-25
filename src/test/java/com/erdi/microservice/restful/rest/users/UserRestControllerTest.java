package com.erdi.microservice.restful.rest.users;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.erdi.microservice.restful.rest.users.dtos.AddressDTO;
import com.erdi.microservice.restful.rest.users.dtos.ContactDTO;
import com.erdi.microservice.restful.rest.users.dtos.UserDTO;
import com.erdi.microservice.restful.rest.users.dtos.requests.CreateOrUpdateUserDTO;
import com.erdi.microservice.restful.rest.users.dtos.requests.RegisterUserAccountDTO;
import com.erdi.microservice.restful.rest.users.entities.Profile;
import com.erdi.microservice.restful.rest.users.entities.User;
import com.erdi.microservice.restful.rest.users.repositories.UserRepository;
import com.erdi.microservice.restful.rest.users.services.UserService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRestControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    public void test_getUserById() {
        Long userId = 1L;
        String userURL = "/users/" + userId;

        ResponseEntity<UserDTO> response = restTemplate.getForEntity(userURL, UserDTO.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        UserDTO userDTO = response.getBody();
        assertNotNull(userDTO);

        assertThat(userDTO.getId(), equalTo(1L));
        assertThat(userDTO.getName(), equalTo("Erdi"));
        assertThat(userDTO.getSurname(), equalTo("Test"));
        assertThat(userDTO.getContactDTO().getEmail(), equalTo("erdi.test@gmail.com"));
        assertThat(userDTO.isEnabled(), equalTo(true));
    }

    @Test
    public void test_createUser() {
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
               .username("ali")
               .password("Ali!xyz")
               .name("Ali")
               .surname("Demir")
               .gender("MALE")
               .enabled(true)
               .email("ali.demir@gmail.com")
               .phone("+9022666334455")
               .facebook("facebook").linkedin("linkedin").website("www.alidemir.com")
               .address("Bostanci")
               .city("Istanbul")
               .country("Turkey")
               .zipCode("34727").build();

        URI uri = URI.create("/users");

        HttpEntity<CreateOrUpdateUserDTO> request = new HttpEntity<>(createOrUpdateUserDTO);
        ResponseEntity<UserDTO> response = restTemplate.postForEntity(uri, request, UserDTO.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        UserDTO userDTO = response.getBody();
        assertNotNull(userDTO);

        assertNotNull(userDTO);
        assertEquals("ali", userDTO.getUsername());
        assertEquals("Ali", userDTO.getName());
        assertEquals("Demir", userDTO.getSurname());
        assertEquals("MALE", userDTO.getGender());

        List<String> profiles = userDTO.getProfiles();
        assertNotNull(profiles);
        assertTrue(profiles.contains("USER"));

        assertEquals(true, userDTO.isEnabled());
        ContactDTO contactDTO = userDTO.getContactDTO();

        assertEquals("ali.demir@gmail.com", contactDTO.getEmail());
        assertEquals("+9022666334455", contactDTO.getPhone());
        assertEquals("facebook", contactDTO.getFacebook());
        assertEquals("linkedin", contactDTO.getLinkedin());
        assertEquals("www.alidemir.com", contactDTO.getWebsite());

        assertNotNull(userDTO.getAddressDTO());
        AddressDTO addressDTO = userDTO.getAddressDTO();
        assertEquals("Bostanci", addressDTO.getAddress());
        assertEquals("Istanbul", addressDTO.getCity());
        assertEquals("Turkey", addressDTO.getCountry());
        assertEquals("34727", addressDTO.getZipCode());

        // delete the created user
        userService.deleteUserById(userDTO.getId());
    }

    @Test
    public void test_updateUser() {
        // create a new user to update
        RegisterUserAccountDTO quickAccount = RegisterUserAccountDTO.builder()
                .username("ayse")
                .password("Ayse!123")
                .name("Ayse")
                .surname("Nur")
                .gender("FEMALE")
                .email("aysenur@gmail.com")
                .build();

        String registerAccountURL = "/users/register";
        HttpEntity<RegisterUserAccountDTO> requestCreate = new HttpEntity<>(quickAccount);
        ResponseEntity<UserDTO> responseCreate = restTemplate.postForEntity(registerAccountURL, requestCreate, UserDTO.class);

        assertThat(responseCreate.getStatusCode(), equalTo(HttpStatus.CREATED));
        UserDTO userDTO = responseCreate.getBody();

        assertNotNull(userDTO);

        // test the update
        Long userId = userDTO.getId();
        URI uri = URI.create("/users/" + userId);

        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .username("Ayse")
                .password("AyseQWERTY")
                .name("Ayse")
                .surname("Nur")
                .gender("FEMALE")
                .enabled(true)
                .email("ayse.nur@gmail.com")
                .phone("+9066611188822")
                .facebook("facebook").linkedin("linkedin").website("www.ayse-nur.com")
                .address("Kosuyolu")
                .city("Ankara")
                .country("Turkey")
                .zipCode("06332").build();

        HttpEntity<CreateOrUpdateUserDTO> request = new HttpEntity<>(createOrUpdateUserDTO);
        ResponseEntity<UserDTO> response = restTemplate.exchange(uri, HttpMethod.PUT, request, UserDTO.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        UserDTO userUpdatedDTO = response.getBody();

        assertEquals("ayse", userUpdatedDTO.getUsername());
        assertEquals("Ayse", userUpdatedDTO.getName());
        assertEquals("Nur", userUpdatedDTO.getSurname());
        assertEquals("FEMALE", userUpdatedDTO.getGender());
        assertEquals(true, userUpdatedDTO.isEnabled());

        // profile
        assertNotNull(userUpdatedDTO.getProfiles());
        assertTrue(userUpdatedDTO.getProfiles().contains( "USER"));

        // contact
        ContactDTO contactDTO = userUpdatedDTO.getContactDTO();
        assertNotNull(contactDTO);

        assertEquals("ayse.nur@gmail.com", contactDTO.getEmail());
        assertEquals("+9066611188822", contactDTO.getPhone());
        assertEquals("facebook", contactDTO.getFacebook());
        assertEquals("linkedin", contactDTO.getLinkedin());
        assertEquals("www.ayse-nur.com", contactDTO.getWebsite());

        // address
        assertNotNull(userUpdatedDTO.getAddressDTO());
        assertEquals("Kosuyolu", userUpdatedDTO.getAddressDTO().getAddress());
        assertEquals("Ankara", userUpdatedDTO.getAddressDTO().getCity());
        assertEquals("Turkey", userUpdatedDTO.getAddressDTO().getCountry());
        assertEquals("06332", userUpdatedDTO.getAddressDTO().getZipCode());

        // delete the user
        userService.deleteUserById(userUpdatedDTO.getId());
    }

    @Test
    public void test_deleteUser() {
        // create a new user to test the deletion
        RegisterUserAccountDTO quickAccount = RegisterUserAccountDTO.builder()
                .username("ayse2")
                .password("Ayse2123!")
                .name("Ayse2")
                .surname("Nur")
                .gender("FEMALE")
                .email("ayse.nur@gmail.com")
                .build();

        String registerAccountURL = "/users/register";
        HttpEntity<RegisterUserAccountDTO> request = new HttpEntity<>(quickAccount);
        ResponseEntity<UserDTO> response = restTemplate.postForEntity(registerAccountURL, request, UserDTO.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        UserDTO userDTO = response.getBody();

        assertNotNull(userDTO);

        // call the delete endpoint
        String deleteUserURL = "/users/" + userDTO.getId();
        restTemplate.delete(deleteUserURL);

        // retrieve a not existing user must to be empty response
        Optional<User> userOpt = userRepository.findById(userDTO.getId());
        assertFalse(userOpt.isPresent());
    }

    // test add profile on User
    @Test
    public void test_addProfileOnUser() {
        // create a new user
        RegisterUserAccountDTO registerUserAccountDTO = RegisterUserAccountDTO.builder()
                .username("ayse")
                .password("Ayse!123")
                .name("Ayse")
                .surname("Nur")
                .gender("FEMALE")
                .email("aysenur@gmail.com")
                .build();

        String registerAccountURL = "/users/register";
        HttpEntity<RegisterUserAccountDTO> requestCreate = new HttpEntity<>(registerUserAccountDTO);
        ResponseEntity<UserDTO> responseCreate = restTemplate.postForEntity(registerAccountURL, requestCreate, UserDTO.class);

        assertThat(responseCreate.getStatusCode(), equalTo(HttpStatus.CREATED));
        UserDTO userDTO = responseCreate.getBody();

        assertNotNull(userDTO);

        // test the add profile
        Long userId = userDTO.getId();
        URI uri = URI.create("/users/" + userId + "/profiles/" + Profile.ADMINISTRATOR);
        ResponseEntity<UserDTO> response = restTemplate.exchange(uri, HttpMethod.POST, null, UserDTO.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        UserDTO addedProfileOnUserDTO = response.getBody();

        assertEquals("ayse", addedProfileOnUserDTO.getUsername());
        assertEquals("Ayse", addedProfileOnUserDTO.getName());
        assertEquals("Nur", addedProfileOnUserDTO.getSurname());
        assertEquals("FEMALE", addedProfileOnUserDTO.getGender());
        assertEquals(true, addedProfileOnUserDTO.isEnabled());

        // check the profile list
        assertNotNull(addedProfileOnUserDTO.getProfiles());
        assertEquals(2L, addedProfileOnUserDTO.getProfiles().size());
        assertTrue(addedProfileOnUserDTO.getProfiles().contains("USER"));
        assertTrue(addedProfileOnUserDTO.getProfiles().contains("ADMINISTRATOR"));

        // delete the user
        userService.deleteUserById(addedProfileOnUserDTO.getId());
    }

    @Test
    public void test_addProfileOnUser_wrongUserId() {
        // perform add profile ADMINISTRATOR on not existing user
        Long userId = 99L; // not existing user
        URI uri = URI.create("/users/" + userId + "/profiles/" + Profile.ADMINISTRATOR);
        ResponseEntity<UserDTO> removeResponse = restTemplate.exchange(uri, HttpMethod.POST, null, UserDTO.class);

        assertThat(removeResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void test_addProfileOnUser_wrongProfileId() {
        // perform add profile with not existing profile
        URI uri = URI.create("/users/" + 1L + "/profiles/" + 99L);
        ResponseEntity<UserDTO> removeResponse = restTemplate.exchange(uri, HttpMethod.POST, null, UserDTO.class);

        assertThat(removeResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void test_removeProfileOnUser() {
        // create a new user
        RegisterUserAccountDTO registerUserAccountDTO = RegisterUserAccountDTO.builder()
                .username("ayse")
                .password("Ayse!123")
                .name("Ayse")
                .surname("Nur")
                .gender("FEMALE")
                .email("aysenur@gmail.com")
                .build();

        String registerAccountURL = "/users/register";
        HttpEntity<RegisterUserAccountDTO> requestCreate = new HttpEntity<>(registerUserAccountDTO);
        ResponseEntity<UserDTO> responseCreate = restTemplate.postForEntity(registerAccountURL, requestCreate, UserDTO.class);

        assertThat(responseCreate.getStatusCode(), equalTo(HttpStatus.CREATED));
        UserDTO userDTO = responseCreate.getBody();

        assertNotNull(userDTO);

        // test the add profile
        Long userId = userDTO.getId();
        URI uri = URI.create("/users/" + userId + "/profiles/" + Profile.ADMINISTRATOR);
        ResponseEntity<UserDTO> response = restTemplate.exchange(uri, HttpMethod.POST, null, UserDTO.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        UserDTO addedProfileOnUserDTO = response.getBody();

        // check the profile list
        assertNotNull(addedProfileOnUserDTO.getProfiles());
        assertEquals(2L, addedProfileOnUserDTO.getProfiles().size());
        assertTrue(addedProfileOnUserDTO.getProfiles().contains("USER"));
        assertTrue(addedProfileOnUserDTO.getProfiles().contains("ADMINISTRATOR"));

        // perform the remove profile ADMIN
        uri = URI.create("/users/" + userId + "/profiles/" + Profile.ADMINISTRATOR);
        ResponseEntity<UserDTO> removeResponse = restTemplate.exchange(uri, HttpMethod.DELETE, null, UserDTO.class);

        assertThat(removeResponse.getStatusCode(), is(HttpStatus.OK));

        UserDTO removedProfileOnUserDTO = removeResponse.getBody();

        // check the profile list
        assertNotNull(removedProfileOnUserDTO.getProfiles());
        assertEquals(1L, removedProfileOnUserDTO.getProfiles().size());
        assertTrue(removedProfileOnUserDTO.getProfiles().contains("USER"));

        // delete the user
        userService.deleteUserById(removedProfileOnUserDTO.getId());
    }

    @Test
    public void test_removeProfileOnUser_wrongUserId() {
        // perform the remove profile ADMINISTRATOR on not existing user
        Long userId = 99L; // not existing user
        URI uri = URI.create("/users/" + userId + "/profiles/" + Profile.ADMINISTRATOR);
        ResponseEntity<UserDTO> removeResponse = restTemplate.exchange(uri, HttpMethod.DELETE, null, UserDTO.class);

        assertThat(removeResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void test_removeProfileOnUser_wrongProfileId() {
        // perform the remove not existing profile
        URI uri = URI.create("/users/" + 1L + "/profiles/" + 99L);
        ResponseEntity<UserDTO> removeResponse = restTemplate.exchange(uri, HttpMethod.DELETE, null, UserDTO.class);

        assertThat(removeResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void test_delete_securedUser_return_BAD_REQUEST() {
        // create a new user to test the deletion
        RegisterUserAccountDTO quickAccount = RegisterUserAccountDTO.builder()
                .username("ayse2")
                .password("Ayse2123!")
                .name("Ayse2")
                .surname("Nur")
                .gender("FEMALE")
                .email("ayse.test@gmail.com")
                .build();

        String registerAccountURL = "/users/register";
        HttpEntity<RegisterUserAccountDTO> request = new HttpEntity<>(quickAccount);
        ResponseEntity<UserDTO> response = restTemplate.postForEntity(registerAccountURL, request, UserDTO.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        UserDTO userDTO = response.getBody();

        assertNotNull(userDTO);

        // call the delete endpoint
        String deleteUserURL = "/users/" + userDTO.getId();
        restTemplate.delete(deleteUserURL);

        // retrieve a not existing user must to be empty response
        Optional<User> userOpt = userRepository.findById(userDTO.getId());
        assertFalse(userOpt.isPresent());
    }

}
