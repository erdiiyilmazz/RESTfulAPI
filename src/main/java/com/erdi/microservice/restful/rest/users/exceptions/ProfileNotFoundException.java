package com.erdi.microservice.restful.rest.users.exceptions;

public class ProfileNotFoundException extends RuntimeException {

    public ProfileNotFoundException(String message) {
        super(message);
    }

}
