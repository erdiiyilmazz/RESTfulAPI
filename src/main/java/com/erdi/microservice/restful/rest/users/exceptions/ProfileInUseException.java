package com.erdi.microservice.restful.rest.users.exceptions;

public class ProfileInUseException extends RuntimeException {

    public ProfileInUseException(String message) {
        super(message);
    }

}
