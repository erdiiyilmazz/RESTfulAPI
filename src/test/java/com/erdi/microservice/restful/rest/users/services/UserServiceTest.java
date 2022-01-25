package com.erdi.microservice.restful.rest.users.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.erdi.microservice.restful.rest.users.dtos.UserDTO;
import com.erdi.microservice.restful.rest.users.dtos.requests.CreateOrUpdateUserDTO;
import com.erdi.microservice.restful.rest.users.dtos.requests.RegisterUserAccountDTO;
import com.erdi.microservice.restful.rest.users.entities.Gender;
import com.erdi.microservice.restful.rest.users.entities.Profile;
import com.erdi.microservice.restful.rest.users.entities.User;
import com.erdi.microservice.restful.rest.users.exceptions.*;
import com.erdi.microservice.restful.rest.users.repositories.ProfileRepository;
import com.erdi.microservice.restful.rest.users.repositories.UserRepository;
import com.erdi.microservice.restful.rest.users.services.EncryptionService;
import com.erdi.microservice.restful.rest.users.services.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.erdi.microservice.restful.rest.users.services.UserTestHelper.getUserTestData;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Autowired
    @InjectMocks
    private UserService userService = new UserService();

    @Before
    public void setUp() {
        // using the default salt for test
        ReflectionTestUtils.setField(userService, "salt", EncryptionService.DEFAULT_SALT);
    }

    @Test
    public void given_existing_users_when_getUserPresentationList_return_validList() {
        User user1 = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");
        User user2= getUserTestData(2L, "tony", "Tony",
                "Soprano", "tony.test@gmail.com", "+9062661272233");
        User user3 = getUserTestData(3L, "johnny", "Johnny",
                "Cash", "johnny.test@gmail.com", "+1666552334477");

        List<User> list = Arrays.asList(user1, user2, user3);

        given(userService.getUserList()).willReturn(list);

        List<UserDTO> userDTOList = userService.getUserPresentationList();

        assertNotNull(userDTOList);
        assertEquals(3, userDTOList.size());

        // take the second element to test the DTO content
        UserDTO userDTO = userDTOList.get(1);

        assertEquals(Long.valueOf(2L) , userDTO.getId());
        assertEquals("tony" , userDTO.getUsername());
        assertEquals("Tony" , userDTO.getName());
        assertEquals("Soprano" , userDTO.getSurname());

        assertNotNull(userDTO.getContactDTO());
        assertEquals("tony.test@gmail.com" , userDTO.getContactDTO().getEmail());
        assertEquals("+9062661272233" , userDTO.getContactDTO().getPhone());
    }

    @Test
    public void given_existing_user_when_getUserById_returnUser() {
        Long userId = 1L;

        User user = getUserTestData(userId, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        User userRet = userService.getUserById(userId);

        assertNotNull(userRet);
        assertEquals(userId, userRet.getId());
        assertEquals("erdi", userRet.getUsername());
        assertEquals("Erdi", userRet.getName());
        assertEquals("Yilmaz", userRet.getSurname());
        assertEquals("erdiyilmaz@gmail.com", userRet.getContact().getEmail());
        assertEquals("+905059667788", userRet.getContact().getPhone());
    }

    @Test(expected = UserNotFoundException.class)
    public void given_not_existing_user_when_getUserById_throw_exception() {
        Long userId = 2L;

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        userService.getUserById(userId);
    }

    @Test(expected = InvalidUserIdentifierException.class)
    public void given_null_user_id_when_getUserById_throw_exception() {
        userService.getUserById(null);
    }

    @Test(expected = InvalidUsernameException.class)
    public void given_null_username_when_getUserByUsername_return_user() {
        userService.getUserByUsername(null);
    }

    @Test
    public void given_existing_username_when_getUserByUsername_return_user() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findByUsername("erdi")).willReturn(userDataForTest);

        User user = userService.getUserByUsername("erdi");

        assertNotNull(user);
        assertEquals(Long.valueOf(1L), user.getId());
        assertEquals("erdi", user.getUsername());
        assertEquals("Erdi", user.getName());
        assertEquals("Yilmaz", user.getSurname());
        assertEquals("erdiyilmaz@gmail.com", user.getContact().getEmail());
        assertEquals("+905059667788", user.getContact().getPhone());
    }

    @Test
    public void given_existing_email_when_getUserByEmail_return_user() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
            "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findByEmail("erdiyilmaz@gmail.com")).willReturn(userDataForTest);

        User user = userService.getUserByEmail("erdiyilmaz@gmail.com");

        assertNotNull(user);
        assertEquals(Long.valueOf(1L), user.getId());
        assertEquals("erdi", user.getUsername());
        assertEquals("Erdi", user.getName());
        assertEquals("Yilmaz", user.getSurname());
        assertEquals("erdiyilmaz@gmail.com", user.getContact().getEmail());
        assertEquals("+905059667788", user.getContact().getPhone());
    }

    @Test(expected = InvalidEmailException.class)
    public void given_invalid_email_getUserByEmail_throw_InvalidUserEmailException() {
        User user = userService.getUserByEmail(null);
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_null_CreateUserAccountDTO_when_createNewUserAccount_throw_InvalidUserDataException() {
        userService.registerUserAccount(null);
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_already_existing_username_when_createNewUserAccount_throw_InvalidUserDataException() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findByUsername("erdi")).willReturn(userDataForTest);

        RegisterUserAccountDTO registerUserAccountDTO = RegisterUserAccountDTO.builder()
                .name("Erdi")
                .surname("Yilmaz")
                .email("erdiyilmaz@gmail.com")
                .gender("MALE")
                .username("erdi")
                .password(UserTestHelper.TEST_PASSWORD_DECRYPTED)
                .build();

        userService.registerUserAccount(registerUserAccountDTO);
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_existing_email_when_createNewUserAccount_throw_InvalidUserDataException() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findByEmail("erdiyilmaz@gmail.com")).willReturn(userDataForTest);

        // existing email
        RegisterUserAccountDTO registerUserAccountDTO = RegisterUserAccountDTO.builder()
                .name("Tony")
                .password("Tony!123")
                .surname("Soprano")
                .email("erdiyilmaz@gmail.com")
                .gender("MALE")
                .username("tony")
                .password(UserTestHelper.TEST_PASSWORD_DECRYPTED)
                .build();

        userService.registerUserAccount(registerUserAccountDTO);
    }

    @Test(expected = ProfileNotFoundException.class)
    public void given_invalidRole_when_setUserRole_throw_RoleNotFoundException() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        // profile doesn't exists
        userService.addUserProfile(userDataForTest, 1);
    }

    @Test
    public void given_valid_profile_id_when_setUserRole_returnUser() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(profileRepository.findById(Profile.USER)).willReturn(Optional.of(new Profile(Profile.USER, "USER")));

        userService.addUserProfile(userDataForTest, Profile.USER);

        assertNotNull(userDataForTest);

        Profile profileUser = new Profile(Profile.USER, "USER");
        assertTrue(userDataForTest.getProfiles().contains(profileUser));

        assertEquals("erdi", userDataForTest.getUsername());
        assertEquals("Erdi", userDataForTest.getName());
        assertEquals("Yilmaz", userDataForTest.getSurname());
        assertTrue(userDataForTest.isEnabled());

        assertNotNull(userDataForTest.getContact());
        assertEquals("erdiyilmaz@gmail.com", userDataForTest.getContact().getEmail());
        assertEquals("+905059667788", userDataForTest.getContact().getPhone());
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_invalid_CreateOrUpdateUserDTO_when_createUser_throw_InvalidUserDataException() {
        userService.createUser(null);
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_already_registered_username_when_createUser_throw_InvalidUserDataException() {
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder().username("erdi").build();

        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findByUsername("erdi")).willReturn(userDataForTest);

        userService.createUser(createOrUpdateUserDTO);
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_already_registered_email_when_createUser_throw_InvalidUserDataException() {
        // existing email
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .name("Tony")
                .surname("Soprano")
                .email("erdiyilmaz@gmail.com")
                .gender("MALE")
                .username("tony")
                .phone("+9062661272233")
                .enabled(true).build();

        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findByEmail("erdiyilmaz@gmail.com")).willReturn(userDataForTest);

        userService.createUser(createOrUpdateUserDTO);
    }

    @Test(expected = InvalidGenderException.class)
    public void given_invalid_gender_string_when_getValidGender_throw_InvalidUserGenderException() {
        Gender.getValidGender("WRONG_GENDER");
    }

    @Test
    public void given_valid_gender_strings_when_getValidGender_return_Gender() {
        // male
        Gender maleGender = Gender.getValidGender("MALE");

        assertNotNull(maleGender);
        assertEquals(1L , maleGender.getGender());

        // female
        Gender femaleGender = Gender.getValidGender("FEMALE");

        assertNotNull(femaleGender);
        assertEquals(2L , femaleGender.getGender());
    }

    @Test(expected = InvalidUserIdentifierException.class)
    public void given_invalid_userId_when_updateUser_throw_InvalidUserIdentifierException() {
        userService.updateUser(null, new CreateOrUpdateUserDTO());
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_invalid_createOrUpdateUserDTO_when_updateUser_throw_InvalidUserDataException() {
        userService.updateUser(1L, null);
    }

    @Test(expected = UserNotFoundException.class)
    public void given_not_existing_userId_when_updateUser_throw_UserNotFoundException() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        userService.updateUser(1L, new CreateOrUpdateUserDTO());
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_existing_username_when_updateUser_throw_InvalidUserDataException() {
        // setting an existing username
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .name("Tony")
                .surname("Soprano")
                .email("tony.test@gmail.com")
                .gender("MALE")
                .username("tony")
                .phone("+9062661272233")
                .enabled(true)
                .build();

        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");
        User userDataForTest2 = getUserTestData(2L, "tony", "Tony",
                "Soprano", "tony.test@gmail.com", "+9062661272233");

        given(userRepository.findById(2L)).willReturn(Optional.of(userDataForTest2));
        given(userRepository.findByUsername("erdi")).willReturn(userDataForTest);

        userService.updateUser(2L, createOrUpdateUserDTO);
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_existing_email_when_updateUser_throw_InvalidUserDataException() {
        // setting an existing email
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .name("Tony")
                .surname("Soprano")
                .email("tony.test@gmail.com")
                .gender("MALE")
                .username("tony")
                .password("Test!123")
                .phone("+9062661272233")
                .enabled(true)
                .build();

        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");
        User userDataForTest2 = getUserTestData(2L, "tony", "Tony",
                "Soprano", "tony.test@gmail.com", "+9062661272233");

        given(userRepository.findById(2L)).willReturn(Optional.of(userDataForTest2));
        given(userRepository.findByEmail("erdiyilmaz@gmail.com")).willReturn(userDataForTest);

        userService.updateUser(2L, createOrUpdateUserDTO);
    }

    @Test
    public void given_existing_user_when_updatedUser_return_userUpdated() {
        // correct user data, update the phone number
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .name("Erdi")
                .surname("Yilmaz")
                .email("erdiyilmaz@gmail.com")
                .gender("MALE")
                .username("erdi")
                .password("Test!123")
                .phone("+3539988776655")
                .enabled(true)
                .address("kadikoy").city("Istanbul").country("Turkey").zipCode("00100")
                .build();

        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findById(1L)).willReturn(Optional.of(userDataForTest));

        userService.updateUser(1L, createOrUpdateUserDTO);
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test(expected = InvalidUserIdentifierException.class)
    public void given_null_userId_when_deleteUserById_throw_InvalidUserIdentifierException() {
        userService.deleteUserById(null);
    }

    @Test(expected = UserNotFoundException.class)
    public void given_not_existing_userId_when_deleteUserById_throw_UserNotFoundException() {
        userService.deleteUserById(1L);
    }

    @Test(expected = InvalidLoginException.class)
    public void given_null_username_and_null_password_when_login_throw_InvalidLoginException() {
        userService.login(null, null);
    }

    @Test(expected = InvalidLoginException.class)
    public void given_null_username_login_when_login_throw_InvalidLoginException() {
        userService.login(null, "WRONG_PWD");
    }

    @Test(expected = InvalidLoginException.class)
    public void given_null_password_login_when_login_throw_InvalidLoginException() {
        userService.login("WRONG", null);
    }

    @Test(expected = InvalidLoginException.class)
    public void given_invalid_login_when_login_throw_InvalidLoginException() {
        userService.login("WRONG", "WRONG_PWD");
    }

    @Test
    public void given_valid_login_when_login_return_User() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findByUsername("erdi")).willReturn(userDataForTest);

        User user = userService.login("erdi", UserTestHelper.TEST_PASSWORD_DECRYPTED);

        assertNotNull(user);
        assertEquals("erdi", user.getUsername());
    }

    @Test(expected = InvalidLoginException.class)
    public void given_invalid_login2_when_login_throw_InvalidLoginException() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findByUsername("erdi")).willReturn(userDataForTest);

        User user = userService.login("erdi", "WRONG_PWD");
    }

    @Test(expected = InvalidLoginException.class)
    public void given_not_enabled_login_when_login_throw_InvalidLoginException() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        userDataForTest.setEnabled(false);

        given(userRepository.findByUsername("erdi")).willReturn(userDataForTest);

        User user = userService.login("erdi", UserTestHelper.TEST_PASSWORD_DECRYPTED);
    }

    // tests add profile on User
    @Test(expected = UserNotFoundException.class)
    public void given_notExistingUserId_when_addProfile_throw_UserNotFoundException() {
        User user = userService.addProfile(99L, 2L);
    }

    @Test(expected = ProfileNotFoundException.class)
    public void given_existingUserId_notExistingRoleId_when_addProfile_throw_RoleNotFoundException() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findById(1L)).willReturn(Optional.of(userDataForTest));

        userService.addProfile(1L, 99L);
    }

    @Test
    public void given_validUserAndRoleIds_when_addProfile_returnUser() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findById(1L)).willReturn(Optional.of(userDataForTest));

        Profile profileAdmin = new Profile(Profile.ADMINISTRATOR, "Administrator");

        given(profileRepository.findById(2L)).willReturn(Optional.of(profileAdmin));

        User user = userService.addProfile(1L, 2L);

        assertNotNull(user);

        // check the new added profile
        Set<Profile> profileSet = user.getProfiles();

        assertNotNull(profileSet);
        assertEquals(2, profileSet.size());
        assertTrue(profileSet.contains(profileAdmin));
    }

    // test remove profile from User
    @Test(expected = UserNotFoundException.class)
    public void given_notExistingUserId_when_removeProfile_throw_UserNotFoundException() {
        User user = userService.removeProfile(99L, 2L);
    }

    @Test(expected = ProfileNotFoundException.class)
    public void given_existingUserId_notExistingRoleId_when_removeProfile_throw_RoleNotFoundException() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        given(userRepository.findById(1L)).willReturn(Optional.of(userDataForTest));

        userService.removeProfile(1L, 99L);
    }

    @Test
    public void given_validUserAndRoleIds_when_removeProfile_returnUser() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");

        Profile profileAdmin = new Profile(Profile.ADMINISTRATOR, "Administrator");
        userDataForTest.getProfiles().add(profileAdmin);

        given(userRepository.findById(1L)).willReturn(Optional.of(userDataForTest));
        given(profileRepository.findById(2L)).willReturn(Optional.of(profileAdmin));

        User user = userService.removeProfile(1L, 2L);

        assertNotNull(user);

        // check the remove profile
        Set<Profile> profileSet = user.getProfiles();

        assertNotNull(profileSet);
        assertEquals(1, profileSet.size());
        assertTrue(!profileSet.contains(profileAdmin));
    }

    @Test(expected = UserIsSecuredException.class)
    public void given_validSecuredUser_when_deleteUser_throw_UserIsSecuredException() {
        User userDataForTest = getUserTestData(1L, "erdi", "Erdi",
                "Yilmaz", "erdiyilmaz@gmail.com", "+905059667788");
        // set a secure user
        userDataForTest.setSecured(true);

        given(userRepository.findById(1L)).willReturn(Optional.of(userDataForTest));

        userService.deleteUserById(1L);
    }

}
