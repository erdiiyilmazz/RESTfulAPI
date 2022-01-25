package com.erdi.microservice.restful.rest.users;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.erdi.microservice.restful.rest.users.dtos.requests.CreateOrUpdateUserDTO;
import com.erdi.microservice.restful.rest.users.dtos.requests.RegisterUserAccountDTO;
import com.erdi.microservice.restful.rest.users.entities.Profile;
import com.erdi.microservice.restful.rest.users.entities.User;
import com.erdi.microservice.restful.rest.users.exceptions.UserNotFoundException;
import com.erdi.microservice.restful.rest.users.services.EncryptionService;
import com.erdi.microservice.restful.rest.users.services.UserService;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserServiceSpringContextTest {

    @TestConfiguration
    static class UserServiceTestContextConfiguration {
        @Bean
        public UserService getUserService() {
            return new UserService();
        }
    }

    @Autowired
    private UserService userService;

    @Test
    public void given_valid_user_data_when_createNewUserAccount_return_createdUser() {
        RegisterUserAccountDTO registerUserAccountDTO = new RegisterUserAccountDTO();
        registerUserAccountDTO.setName("Tony");
        registerUserAccountDTO.setSurname("Soprano");
        registerUserAccountDTO.setEmail("tony.test@gmail.com");
        registerUserAccountDTO.setGender("MALE");
        registerUserAccountDTO.setUsername("tony");
        registerUserAccountDTO.setPassword("TSoprano.");

        User createdUser = userService.registerUserAccount(registerUserAccountDTO);

        assertNotNull(createdUser);
        assertEquals("Tony", createdUser.getName());
        assertEquals("Soprano", createdUser.getSurname());
        assertEquals("tony.test@gmail.com", createdUser.getContact().getEmail());
        assertEquals("MALE", createdUser.getGender().name());
        assertEquals("tony", createdUser.getUsername());

        assertTrue(EncryptionService.isPasswordValid("TSoprano.", createdUser.getPassword(),
                EncryptionService.DEFAULT_SALT));
    }

    @Test
    public void given_valid_user_data_when_createUser_return_createdUser() {
        CreateOrUpdateUserDTO createOrUpdateUserDTO = new CreateOrUpdateUserDTO();
        createOrUpdateUserDTO.setName("Johnny");
        createOrUpdateUserDTO.setSurname("Cash");
        createOrUpdateUserDTO.setEmail("johnny.test@gmail.com");
        createOrUpdateUserDTO.setGender("MALE");
        createOrUpdateUserDTO.setUsername("johnny");
        createOrUpdateUserDTO.setPassword("John123");
        createOrUpdateUserDTO.setPhone("+1332242123");
        // set address
        createOrUpdateUserDTO.setAddress("Karl-Marx Strasse");
        createOrUpdateUserDTO.setCity("Berlin");
        createOrUpdateUserDTO.setCountry("Germany");
        createOrUpdateUserDTO.setZipCode("3100");

        User createdUser = userService.createUser(createOrUpdateUserDTO);

        assertNotNull(createdUser);
        assertEquals("Johnny", createdUser.getName());
        assertEquals("Cash", createdUser.getSurname());
        assertEquals("john.test@gmail.com", createdUser.getContact().getEmail());
        assertEquals("MALE", createdUser.getGender().name());
        assertEquals("johnny", createdUser.getUsername());

        assertTrue(EncryptionService.isPasswordValid("John123", createdUser.getPassword(),
                EncryptionService.DEFAULT_SALT));

        assertEquals("+3531122334499", createdUser.getContact().getPhone());

        Profile adminRole = new Profile(Profile.USER, "USER");
        assertTrue(createdUser.getProfiles().contains(adminRole));

        assertEquals("Zuhtupasa" , createdUser.getAddress().getAddress());
        assertEquals("Istanbul", createdUser.getAddress().getCity());
        assertEquals("Turkey", createdUser.getAddress().getCountry());
        assertEquals("34100", createdUser.getAddress().getZipCode());
    }

    @Test
    public void given_valid_user_data_when_updateUser_return_userUpdated() {
        CreateOrUpdateUserDTO updateUserDTO = new CreateOrUpdateUserDTO();
        updateUserDTO.setName("Erdi");
        updateUserDTO.setSurname("Yilmaz");
        updateUserDTO.setEmail("erdiyilmaz@gmail.com");
        updateUserDTO.setGender("MALE");
        updateUserDTO.setUsername("erdi");
        updateUserDTO.setPassword("Test!123");
        updateUserDTO.setPhone("+905543246655"); // update the phone number
        updateUserDTO.setAddress("Zuhtupasa");
        updateUserDTO.setCity("Istanbul");
        updateUserDTO.setCountry("Turkey");
        updateUserDTO.setZipCode("34100");

        User updatedUser = userService.updateUser(1L, updateUserDTO);

        assertNotNull(updatedUser);
        assertEquals("Erdi", updatedUser.getName());
        assertEquals("Yilmaz", updatedUser.getSurname());
        //
        assertEquals("erdiyilmaz@gmail.com", updatedUser.getContact().getEmail());
        assertEquals("erdi", updatedUser.getUsername());
        assertEquals("1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=", updatedUser.getPassword());
        assertEquals("MALE", updatedUser.getGender().name());
        assertEquals("+35344335522", updatedUser.getContact().getPhone());

        Profile adminRole = new Profile(Profile.USER, "USER");
        assertTrue(updatedUser.getProfiles().contains(adminRole));
        // check on address
        assertEquals("Kadikoy", updatedUser.getAddress().getAddress());
        assertEquals("Zuhtupasa", updatedUser.getAddress().getCity());
        assertEquals("Turkey", updatedUser.getAddress().getCountry());
        assertEquals("34500", updatedUser.getAddress().getZipCode());
    }

    @Test(expected = UserNotFoundException.class)
    public void given_valid_user_when_deleteUserById_user_deleted() {
        Long userId= 2L;
        userService.deleteUserById(userId);

        User user = userService.getUserById(userId);
    }

}
