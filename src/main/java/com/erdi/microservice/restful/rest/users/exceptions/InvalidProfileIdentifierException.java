package com.erdi.microservice.restful.rest.users.exceptions;

public class InvalidProfileIdentifierException extends RuntimeException {

    public InvalidProfileIdentifierException(String message) {
        super(message);
    }

}
