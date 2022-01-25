package com.erdi.microservice.restful.rest.users.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.erdi.microservice.restful.rest.users.entities.Contact;
import com.erdi.microservice.restful.rest.users.entities.Gender;
import com.erdi.microservice.restful.rest.users.entities.Profile;
import com.erdi.microservice.restful.rest.users.entities.User;
import com.erdi.microservice.restful.rest.users.services.EncryptionService;

public class UserTestHelper {

    public static final String TEST_PASSWORD_DECRYPTED = "Test!123";

    // create a test user data
    public static User getUserTestData(Long id, String username, String name, String surname, String email, String phone) {
        User user = new User();
        user.setId(id);

        user.setUsername(username);
        user.setPassword(EncryptionService.encrypt(TEST_PASSWORD_DECRYPTED, EncryptionService.DEFAULT_SALT));

        user.setName(name);
        user.setSurname(surname);
        user.setGender(Gender.MALE);

        Contact contact = new Contact();
        contact.setEmail(email);
        contact.setPhone(phone);
        // omitted other contact fields

        user.setContact(contact);

        user.setEnabled(true);

        user.setCreationDt(LocalDateTime.of(2020, 2, 1, 12, 30));
        user.setUpdatedDt(LocalDateTime.of(2020, 2, 1, 16, 45));
        user.setLoginDt(null);

        // add the USER profile
        Set<Profile> profileSet = new HashSet<>();
        profileSet.add(new Profile(Profile.USER, "USER"));

        user.setProfiles(profileSet);
        return user;
    }

}
