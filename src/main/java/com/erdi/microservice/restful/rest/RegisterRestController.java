package com.erdi.microservice.restful.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erdi.microservice.restful.rest.users.dtos.UserDTO;
import com.erdi.microservice.restful.rest.users.dtos.requests.RegisterUserAccountDTO;
import com.erdi.microservice.restful.rest.users.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class RegisterRestController {

    @Autowired
    private UserService userService;

    // register a new user's account: no all the user information are required
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerNewUserAccount(@RequestBody RegisterUserAccountDTO registerUserAccountDTO) {
        return new ResponseEntity(new UserDTO(userService.registerUserAccount(registerUserAccountDTO)), null, HttpStatus.CREATED);
    }

}
