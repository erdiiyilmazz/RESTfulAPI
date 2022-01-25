package com.erdi.microservice.restful.rest.users.dtos;

import org.junit.Test;

import com.erdi.microservice.restful.rest.users.dtos.ContactDTO;
import com.erdi.microservice.restful.rest.users.dtos.UserDTO;
import com.erdi.microservice.restful.rest.users.entities.*;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.*;

public class UserDTOTest {

    @Test
    public void userDTOTestConstructor1() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setName("testName");
        user.setSurname("testSurname");
        user.setEnabled(true);
        user.setGender(Gender.MALE);
        user.setEnabled(true);
        user.setSecured(false);

        Contact contactInput = new Contact();
        contactInput.setEmail("email");
        contactInput.setPhone("+905059667788");
        contactInput.setFacebook("facebook");
        contactInput.setLinkedin("linkedin");
        contactInput.setWebsite("www.test.com");

        user.setContact(contactInput);

        LocalDateTime creationDt = LocalDateTime.of(2020, 2, 1, 12, 30);
        user.setCreationDt(creationDt);

        LocalDateTime updatedDt = LocalDateTime.of(2020, 2, 1, 16, 45);
        user.setUpdatedDt(updatedDt);

        Profile profileUser = new Profile(Profile.USER, "USER");
        Profile profileAdmin = new Profile(Profile.ADMINISTRATOR, "ADMINISTRATOR");

        user.getProfiles().add(profileAdmin);
        user.getProfiles().add(profileUser);

        UserDTO userDTO = new UserDTO(user);

        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getSurname(), user.getSurname());

        assertTrue(userDTO.isEnabled());
        assertTrue(!userDTO.isSecured());

        // contact
        ContactDTO contactDTO = userDTO.getContactDTO();
        assertNotNull(contactDTO);

        assertEquals(userDTO.getContactDTO().getEmail(), user.getContact().getEmail());
        assertEquals(userDTO.getContactDTO().getPhone(), user.getContact().getPhone());
        assertEquals(userDTO.getContactDTO().getFacebook(), user.getContact().getFacebook());
        assertEquals(userDTO.getContactDTO().getLinkedin(), user.getContact().getLinkedin());
        assertEquals(userDTO.getContactDTO().getWebsite(), user.getContact().getWebsite());

        assertEquals(userDTO.isEnabled(), user.isEnabled());

        assertEquals(creationDt, userDTO.getCreationDt());
        assertEquals(updatedDt, userDTO.getUpdatedDt());
        assertEquals(null, userDTO.getLoginDt());

        assertNotNull(user.getProfiles());

        Set<Profile> profilesTest = user.getProfiles();

        assertTrue(profilesTest.contains(profileUser));
        assertTrue(profilesTest.contains(profileAdmin));
    }

    @Test
    public void userDTOTestConstructor2() {
        // test enabled and disabled permissions
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setName("testName");
        user.setSurname("testSurname");
        user.setEnabled(true);
        user.setGender(Gender.MALE);
        user.setEnabled(true);
        user.setSecured(false);

        Contact contactInput = new Contact();
        contactInput.setEmail("email");
        contactInput.setPhone("+905059667788");
        contactInput.setFacebook("facebook");
        contactInput.setLinkedin("linkedin");
        contactInput.setWebsite("www.test.com");

        user.setContact(contactInput);

        LocalDateTime creationDt = LocalDateTime.of(2022, 1, 22, 12, 30);
        user.setCreationDt(creationDt);

        LocalDateTime updatedDt = LocalDateTime.of(2022, 1, 23, 13, 45);
        user.setUpdatedDt(updatedDt);

        // create a set of profiles and permissions
        Profile profileUser = new Profile(Profile.USER, "USER");
        Profile profileAdmin = new Profile(Profile.ADMINISTRATOR, "ADMINISTRATOR");

        Permission p1 = new Permission(1L, "LOGIN", true);
        Permission p2 = new Permission(2L, "VIEW_ROLE", true);
        Permission p3 = new Permission(3L, "ADMIN_STATISTICS", false);
        Permission p4 = new Permission(4L, "ADMIN_PROFILES", true);

        profileUser.getPermissions().add(p1);
        profileUser.getPermissions().add(p2);

        profileAdmin.getPermissions().add(p3);
        profileAdmin.getPermissions().add(p4);

        user.getProfiles().add(profileAdmin);
        user.getProfiles().add(profileUser);

        UserDTO userDTO = new UserDTO(user);

        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getSurname(), user.getSurname());

        assertTrue(userDTO.isEnabled());
        assertTrue(!userDTO.isSecured());

        // contact
        ContactDTO contactDTO = userDTO.getContactDTO();
        assertNotNull(contactDTO);

        assertEquals(userDTO.getContactDTO().getEmail(), user.getContact().getEmail());
        assertEquals(userDTO.getContactDTO().getPhone(), user.getContact().getPhone());
        assertEquals(userDTO.getContactDTO().getFacebook(), user.getContact().getFacebook());
        assertEquals(userDTO.getContactDTO().getLinkedin(), user.getContact().getLinkedin());
        assertEquals(userDTO.getContactDTO().getWebsite(), user.getContact().getWebsite());

        assertEquals(userDTO.isEnabled(), user.isEnabled());

        assertEquals(creationDt, userDTO.getCreationDt());
        assertEquals(updatedDt, userDTO.getUpdatedDt());
        assertEquals(null, userDTO.getLoginDt());

        assertNotNull(user.getProfiles());

        Set<Profile> profilesTest = user.getProfiles();

        assertTrue(profilesTest.contains(profileUser));
        assertTrue(profilesTest.contains(profileAdmin));

        assertEquals(2, userDTO.getProfiles().size());
        assertTrue(userDTO.getProfiles().contains("USER"));
        assertTrue(userDTO.getProfiles().contains("ADMINISTRATOR"));

        assertEquals(2, userDTO.getProfiles().size());
        assertEquals(3, userDTO.getPermissions().size());

        assertEquals(3, userDTO.getPermissions().size());
        assertTrue(userDTO.getPermissions().contains("LOGIN"));
        assertTrue(userDTO.getPermissions().contains("VIEW_ROLE"));

        assertTrue(userDTO.getPermissions().contains("ADMIN_PROFILES"));
        assertFalse(userDTO.getPermissions().contains("ADMIN_STATISTICS"));
    }

}
