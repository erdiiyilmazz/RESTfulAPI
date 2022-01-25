package com.erdi.microservice.restful.rest.users.services;


import com.erdi.microservice.restful.rest.users.entities.Permission;
import com.erdi.microservice.restful.rest.users.entities.Profile;
import com.erdi.microservice.restful.rest.users.exceptions.*;
import com.erdi.microservice.restful.rest.users.repositories.PermissionRepository;
import com.erdi.microservice.restful.rest.users.repositories.ProfileRepository;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public Iterable<Profile> getProfileList() {
        return profileRepository.findAll();
    }

    public Profile getProfileById(Long id) {
        if (id == null) {
            throw new InvalidProfileIdentifierException("Id profile cannot be null");
        }
        Optional<Profile> profileOpt = profileRepository.findById(id);
        if (profileOpt.isPresent()) {
            return profileOpt.get();
        }
        throw new ProfileNotFoundException(String.format("Profile not found for Id = %s", id));
    }

    public static void validateProfileName(String profileName) {
        if (Strings.isNullOrEmpty(profileName)) {
            throw new InvalidProfileDataException(String.format("Invalid profile name: %s", profileName));
        }
    }

    @Transactional
    public Profile createRole(String profileStr) {
        validateProfileName(profileStr);

        // check if profileStr is available or not 
        if (profileRepository.findByRole(profileStr).isPresent()) {
            String errMsg = String.format("The profile %s already exists!!", profileStr);
            log.error(errMsg);
            throw new ProfileInUseException(errMsg);
        }

        Profile profile = new Profile();
        profile.setProfile(profileStr);

        profile = profileRepository.save(profile);
        log.info(String.format("Role %s %s has been created.", profile.getId(), profile.getProfile()));

        return profile;
    }

    @Transactional
    public void deleteProfile(Long id) {
        Optional<Profile> profileOpt = profileRepository.findById(id);
        if (!profileOpt.isPresent()) {
            String errMsg = String.format("Role is not found for Id = %s hence cannot be deleted", id);
            log.error(errMsg);
            throw new ProfileNotFoundException(errMsg);
        }

        Profile profile = profileOpt.get();

        // check if the profile is available 
        Long countUsages = profileRepository.countRoleUsage(id);
        if (countUsages > 0) {
            String errMsg = String.format("The profile %s %s is in use (%s users_profiles configuration rows)" +
                            " and cannot be deleted", profile.getId(), profile.getProfile(), countUsages);
            log.error(errMsg);
            throw new ProfileInUseException(errMsg);
        }

        profileRepository.deleteById(id);
        log.info(String.format("Role %s has been deleted.", id));
    }

    // add or remove a permission on a profile

    public static void validatePermissionKey(String permissionKey) {
        if (Strings.isNullOrEmpty(permissionKey)) {
            throw new InvalidPermissionDataException("Permission key cannot be null or empty");
        }
    }

    @Transactional
    public Profile addPermissionOnRole(Long profileId, String permissionKey) {
        validatePermissionKey(permissionKey);

        // check profile
        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        if (!profileOpt.isPresent()) {
            throw new ProfileNotFoundException(String.format("Role not found with Id = %s", profileId));
        }
        Profile profile = profileOpt.get();

        // check if exists the permission key
        Permission permission;

        Optional<Permission> permissionOpt = permissionRepository.findByPermission(permissionKey);
        if (permissionOpt.isPresent()) {
            // the permission exists
            permission = permissionOpt.get();
        } else {
            // if the permission doesn't exists: create one
            permission = new Permission();
            permission.setPermission(permissionKey);

            permission = permissionRepository.save(permission);
        }

        // check if this profile contains already the given permission
        if (profile.getPermissions().contains(permission)) {
            throw new InvalidPermissionDataException(String.format("The permission %s has been already" +
                            " associated on the profile %s", permission.getPermission(), profile.getProfile() ));
        }

        profile.getPermissions().add(permission);
        profileRepository.save(profile);

        log.info(String.format("Added permission %s on profile id = %s", permissionKey, profileId));
        return profileRepository.findById(profileId).get();
    }

    @Transactional
    public Profile removePermissionOnRole(Long profileId, String permissionKey) {
        validatePermissionKey(permissionKey);

        // check profile
        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        if (!profileOpt.isPresent()) {
            throw new ProfileNotFoundException(String.format("Role not found with Id = %s", profileId));
        }
        Profile profile = profileOpt.get();

        // check permission
        Optional<Permission> permissionOpt = permissionRepository.findByPermission(permissionKey);
        if (!permissionOpt.isPresent()) {
            throw new PermissionNotFoundException(String.format("Permission not found with Id = %s on profile %s",
                    permissionKey, profileId));
        }

        Permission permission = permissionOpt.get();

        profile.getPermissions().remove(permission);
        profileRepository.save(profile);

        log.info(String.format("Removed permission %s from profile id = %s", permissionKey, profileId));
        return profileRepository.findById(profileId).get();
    }

}
