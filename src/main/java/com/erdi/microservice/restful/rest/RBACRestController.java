package com.erdi.microservice.restful.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.erdi.microservice.restful.rest.users.dtos.PermissionDTO;
import com.erdi.microservice.restful.rest.users.dtos.ProfileDTO;
import com.erdi.microservice.restful.rest.users.entities.Permission;
import com.erdi.microservice.restful.rest.users.entities.Profile;
import com.erdi.microservice.restful.rest.users.services.EncryptionService;
import com.erdi.microservice.restful.rest.users.services.PermissionService;
import com.erdi.microservice.restful.rest.users.services.ProfileService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users/mgmtApi")
public class RBACRestController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private PermissionService permissionService;

    // profiles
    @GetMapping("/profiles")
    public ResponseEntity<List<ProfileDTO>> getProfilePresentationList() {
        Iterable<Profile> profileList = profileService.getProfileList();
        ArrayList<ProfileDTO> list = new ArrayList<>();
        profileList.forEach(e -> list.add(new ProfileDTO(e)));
        return ResponseEntity.ok(list);
    }

    @PostMapping("/profiles")
    public ResponseEntity<ProfileDTO> createRole(@RequestBody String profile) {
        return new ResponseEntity(new ProfileDTO(profileService.createRole(profile)), null, HttpStatus.CREATED);
    }

    @GetMapping("/profiles/{profileId}")
    public ProfileDTO getProfileById(@PathVariable("profileId") Long profileId) {
        return new ProfileDTO(profileService.getProfileById(profileId));
    }

    @DeleteMapping("/profiles/{profileId}")
    public ResponseEntity<?> deleteProfileById(@PathVariable("profileId") Long profileId) {
        profileService.deleteProfile(profileId);
        return ResponseEntity.noContent().build();
    }

    // retrieve the permission's list
    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionDTO>> getPermissionPresentationList() {
        Iterable<Permission> permissionList = permissionService.getPermissionList();
        ArrayList<PermissionDTO> list = new ArrayList<>();
        permissionList.forEach(e -> list.add(new PermissionDTO(e)));
        return ResponseEntity.ok(list);
    }

    // permissions

    @GetMapping("/permissions/{permissionKey}")
    public ResponseEntity<PermissionDTO> getPermissionByKey(@PathVariable("permissionKey") String permissionKey) {
        PermissionDTO permissionDTO = new PermissionDTO(permissionService.getPermissionByKey(permissionKey));
        return ResponseEntity.ok(permissionDTO);
    }

    @PostMapping("/permissions")
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {
        return new ResponseEntity(new PermissionDTO(permissionService.createPermission(permissionDTO)), HttpStatus.CREATED);
    }

    @PutMapping("/permissions")
    public ResponseEntity<PermissionDTO> updatePermission(@RequestBody PermissionDTO permissionDTO) {
        return new ResponseEntity(new PermissionDTO(permissionService.updatePermission(permissionDTO)), HttpStatus.CREATED);
    }

    @DeleteMapping("/permissions/{permissionKey}")
    public ResponseEntity<?> deletePermissionByKey(@PathVariable("permissionKey") String permissionKey) {
        permissionService.deletePermissionByKey(permissionKey);
        return ResponseEntity.noContent().build();
    }

    // add or remove a Permission on a Role

    @PostMapping("/profiles/{profileId}/permissions/{permissionKey}")
    public ResponseEntity<ProfileDTO> addPermissionOnRole(@PathVariable("profileId") Long profileId, @PathVariable("permissionKey") String permissionKey) {
        return new ResponseEntity(new ProfileDTO(profileService.addPermissionOnRole(profileId, permissionKey)), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/profiles/{profileId}/permissions/{permissionKey}")
    public ResponseEntity<ProfileDTO> removePermissionOnRole(@PathVariable("profileId") Long profileId, @PathVariable("permissionKey") String permissionKey) {
        return new ResponseEntity(new ProfileDTO(profileService.removePermissionOnRole(profileId, permissionKey)), null, HttpStatus.OK);
    }

    // salt generation
    @GetMapping("/salt")
    public ResponseEntity<String> generateSalt() {
        return new ResponseEntity<String>(EncryptionService.generateSalt(32), HttpStatus.CREATED);
    }

}
