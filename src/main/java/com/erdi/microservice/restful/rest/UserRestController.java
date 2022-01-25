package com.erdi.microservice.restful.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.erdi.microservice.restful.rest.users.dtos.UserDTO;
import com.erdi.microservice.restful.rest.users.dtos.UserListDTO;
import com.erdi.microservice.restful.rest.users.dtos.requests.CreateOrUpdateUserDTO;
import com.erdi.microservice.restful.rest.users.services.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserListDTO> getUserPresentationList() {
        List<UserDTO> list = userService.getUserPresentationList();
        UserListDTO userListDTO = new UserListDTO();
        list.stream().forEach(e -> userListDTO.getUserList().add(e));
        return ResponseEntity.ok(userListDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateOrUpdateUserDTO createOrUpdateUserDTO) {
        return new ResponseEntity(new UserDTO(userService.createUser(createOrUpdateUserDTO)), null, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Long id) {
        return new UserDTO(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long id, @RequestBody CreateOrUpdateUserDTO updateUserDTO) {
        return new ResponseEntity(new UserDTO(userService.updateUser(id, updateUserDTO)), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // add or remove a Role on a user
    @PostMapping("/{id}/profiles/{profileId}")
    public ResponseEntity<UserDTO> addProfile(@PathVariable("id") Long id, @PathVariable("profileId") Long profileId) {
        return new ResponseEntity(new UserDTO(userService.addProfile(id, profileId)), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/profiles/{profileId}")
    public ResponseEntity<UserDTO> removeProfile(@PathVariable("id") Long id, @PathVariable("profileId") Long profileId) {
        return new ResponseEntity(new UserDTO(userService.removeProfile(id, profileId)), null, HttpStatus.OK);
    }

}
