package com.erdi.microservice.restful.rest.users.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.erdi.microservice.restful.rest.users.entities.Permission;
import com.erdi.microservice.restful.rest.users.entities.Profile;
import com.erdi.microservice.restful.rest.users.exceptions.*;
import com.erdi.microservice.restful.rest.users.repositories.PermissionRepository;
import com.erdi.microservice.restful.rest.users.repositories.ProfileRepository;
import com.erdi.microservice.restful.rest.users.services.ProfileService;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Autowired
    @InjectMocks
    private ProfileService profileService = new ProfileService();

    @Test(expected = InvalidProfileIdentifierException.class)
    public void given_wrong_profileId_when_getProfileById_throw_InvalidRoleIdentifierException() {
        profileService.getProfileById(null);
    }

    @Test(expected = ProfileNotFoundException.class)
    public void given_not_existing_profileId_when_getProfileById_throw_RoleNotFoundException() {
        Long profileId = 99L;
        profileService.getProfileById(profileId);
    }

    @Test
    public void given_existing_profileId_when_getProfileById_return_Role() {
        Long profileId = 99L;
        Profile profile = new Profile(profileId, "TEST ROLE");

        given(profileRepository.findById(profileId)).willReturn(Optional.of(profile));

        Profile returnRole = profileService.getProfileById(profileId);

        assertNotNull(returnRole);
        assertEquals(profile.getId(), returnRole.getId());
    }

    // validateProfileName

    @Test(expected = InvalidProfileDataException.class)
    public void given_invalid_profile_name_when_validateProfileName_throw_InvalidRoleDataException() {
        profileService.validateProfileName(null);
    }

    @Test(expected = InvalidProfileDataException.class)
    public void given_empty_profile_name_when_validateProfileName_throw_InvalidRoleDataException() {
        profileService.validateProfileName("");
    }

    @Test
    public void given_empty_profile_name_when_validateProfileName_no_exception_occurs() {
        profileService.validateProfileName("VALID_ROLE_TEST");
    }

    // createRole

    @Test(expected = InvalidProfileDataException.class)
    public void given_invalid_profile_name_when_createRole_throw_InvalidRoleDataException() {
        profileService.createRole(null);
    }

    @Test(expected = ProfileInUseException.class)
    public void given_valid_used_name_when_createRole_throw_RoleInUseException() {
        Profile profile = new Profile(1L, "TEST");
        given(profileRepository.findByRole("TEST")).willReturn(Optional.of(profile));

        profileService.createRole("TEST");
    }

    @Test
    public void given_valid_not_used_name_when_createRole_returnRole() {
        Long genId = 123L;
        Profile profileData = new Profile(genId, "TEST");

        when(profileRepository.save(any(Profile.class))).thenReturn(new Profile(genId, profileData.getProfile()));

        Profile profile = profileService.createRole("TEST");

        assertNotNull(profile);
        assertEquals(genId, profile.getId());
        assertEquals("TEST", profile.getProfile());
    }

    // deleteRole

    @Test(expected = ProfileNotFoundException.class)
    public void given_not_existing_profile_when_deleteProfile_throw_RoleNotFoundException() {
        profileService.deleteProfile(1L);
    }

    @Test(expected = ProfileInUseException.class)
    public void given_existing_profile_in_use_when_deleteRole_throw_RoleInUseException() {
        given(profileRepository.findById(1L)).willReturn(Optional.of(new Profile(1L, "TEST")));
        given(profileRepository.countRoleUsage(1L)).willReturn(10L);

        profileService.deleteProfile(1L);
    }

    @Test
    public void given_existing_profile_not_in_use_when_deleteRole_Ok() {
        given(profileRepository.findById(1L)).willReturn(Optional.of(new Profile(1L, "TEST")));
        given(profileRepository.countRoleUsage(1L)).willReturn(0L);

        profileService.deleteProfile(1L);
    }

    // validatePermissionKey

    @Test(expected = InvalidPermissionDataException.class)
    public void given_null_permissionKey_when_validatePermissionKey_throw_InvalidPermissionDataException() {
        profileService.validatePermissionKey(null);
    }

    @Test(expected = InvalidPermissionDataException.class)
    public void given_empty_permissionKey_when_validatePermissionKey_throw_InvalidPermissionDataException() {
        profileService.validatePermissionKey("");
    }

    // addPermissionOnRole

    @Test(expected = InvalidPermissionDataException.class)
    public void given_invalid_permission_when_addPermissionOnRole_throw_InvalidPermissionDataException() {
        profileService.addPermissionOnRole(1L, "");
    }

    @Test(expected = ProfileNotFoundException.class)
    public void given_not_existing_profile_when_addPermissionOnRole_throw_RoleNotFoundException() {
        profileService.addPermissionOnRole(1L, "PERMISSION_ONE");
    }

    @Test
    public void given_existing_profile_and_not_existing_permission_return_profile_updated() {
        Profile profile = new Profile(1L, "TEST");
        given(profileRepository.findById(1L)).willReturn(Optional.of(profile));

        Profile profileUpdated = profileService.addPermissionOnRole(1L, "PERMISSION_ONE");

        assertNotNull(profileUpdated);
        // profile data
        assertEquals(Long.valueOf(1L), profileUpdated.getId());
        assertEquals("TEST", profileUpdated.getProfile());

        // permissions
        assertEquals(1L, profileUpdated.getPermissions().size());
    }

    @Test
    public void given_existing_profile_and_existing_permission_return_profile_updated() {
        Profile profile = new Profile(1L, "TEST");
        given(profileRepository.findById(1L)).willReturn(Optional.of(profile));

        Permission permission = new Permission();
        permission.setId(1L);
        permission.setPermission("PERMISSION_ONE");

        given(permissionRepository.findByPermission("PERMISSION_ONE")).willReturn(Optional.of(permission));

        Profile profileUpdated = profileService.addPermissionOnRole(1L, "PERMISSION_ONE");

        assertNotNull(profileUpdated);
        // profile data
        assertEquals(Long.valueOf(1L), profileUpdated.getId());
        assertEquals("TEST", profileUpdated.getProfile());

        // permissions
        assertEquals(1L, profileUpdated.getPermissions().size());
    }

    @Test(expected = InvalidPermissionDataException.class)
    public void given_existing_profile_and_existing_already_associated_permission_throw_InvalidPermissionDataException() {
        Profile profile = new Profile(1L, "TEST");
        profile.getPermissions().add(new Permission(1L, "PERMISSION_ONE"));

        given(profileRepository.findById(1L)).willReturn(Optional.of(profile));

        Permission permission = new Permission(1L, "PERMISSION_ONE");

        given(permissionRepository.findByPermission("PERMISSION_ONE")).willReturn(Optional.of(permission));

        Profile profileUpdated = profileService.addPermissionOnRole(1L, "PERMISSION_ONE");

        assertNotNull(profileUpdated);
        // profile data
        assertEquals(Long.valueOf(1L), profileUpdated.getId());
        assertEquals("TEST", profileUpdated.getProfile());

        // permissions
        assertEquals(1L, profileUpdated.getPermissions().size());
    }

    // removePermissionOnRole

    @Test(expected = InvalidPermissionDataException.class)
    public void given_not_valid_permission_when_removePermissionOnRole_throw_InvalidPermissionDataException() {
        profileService.removePermissionOnRole(1L, "");
    }

    @Test(expected = ProfileNotFoundException.class)
    public void given_not_existing_profile_when_removePermissionOnRole_throw_RoleNotFoundException() {
        profileService.removePermissionOnRole(1L, "PERMISSION");
    }

    @Test(expected = PermissionNotFoundException.class)
    public void given_existing_profile_not_existing_permission_when_removePermissionOnRole_throw_PermissionNotFoundException() {
        Profile profile = new Profile(1L, "TEST");
        given(profileRepository.findById(1L)).willReturn(Optional.of(profile));

        profileService.removePermissionOnRole(1L, "PERMISSION");
    }

    @Test
    public void given_existing_profile_existing_permission_not_used_when_removePermissionOnRole_return_Role() {
        Profile profile = new Profile(1L, "TEST");
        given(profileRepository.findById(1L)).willReturn(Optional.of(profile));

        Permission permission = new Permission();
        permission.setId(1L);
        permission.setPermission("PERMISSION");

        given(permissionRepository.findByPermission("PERMISSION")).willReturn(Optional.of(permission));

        Profile profileUpdated = profileService.removePermissionOnRole(1L, "PERMISSION");

        assertNotNull(profileUpdated);
        // profile data
        assertEquals(Long.valueOf(1L), profileUpdated.getId());
        assertEquals("TEST", profileUpdated.getProfile());

        // permissions
        assertEquals(0L, profileUpdated.getPermissions().size());
    }

    @Test
    public void given_existing_profile_existing_permission_in_used_when_removePermissionOnRole_return_Role() {
        Profile profile = new Profile(1L, "TEST");
        given(profileRepository.findById(1L)).willReturn(Optional.of(profile));

        Permission permission = new Permission();
        permission.setId(1L);
        permission.setPermission("PERMISSION");

        given(permissionRepository.findByPermission("PERMISSION")).willReturn(Optional.of(permission));

        Profile profileUpdated = profileService.removePermissionOnRole(1L, "PERMISSION");

        assertNotNull(profileUpdated);
        // profile data
        assertEquals(Long.valueOf(1L), profileUpdated.getId());
        assertEquals("TEST", profileUpdated.getProfile());

        // permissions
        assertEquals(0L, profileUpdated.getPermissions().size());
    }

    // getProfileList

    @Test
    public void calling_getProfileList_then_return_list_of_profiles() {
        ArrayList<Profile> profileArrayList = new ArrayList<>();
        profileArrayList.add(new Profile(1L, "FIRST_ROLE"));
        profileArrayList.add(new Profile(2L, "SECOND_ROLE"));

        given(profileRepository.findAll()).willReturn(profileArrayList);

        Iterable<Profile> profileIterable = profileService.getProfileList();

        assertNotNull(profileIterable);

        // TODO: check on size and or data
    }

}
