package com.erdi.microservice.restful.rest.users.exceptions;

public class InvalidProfileDataException extends RuntimeException {

    public InvalidProfileDataException(String message) {
        super(message);
    }

}
