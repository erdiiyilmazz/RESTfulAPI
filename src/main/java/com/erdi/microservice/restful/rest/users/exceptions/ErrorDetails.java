package com.erdi.microservice.restful.rest.users.exceptions;

import lombok.Data;

@Data
public class ErrorDetails implements java.io.Serializable {

    private final String message;

    private final long timestamp;

    public ErrorDetails(final String message) {
        this.timestamp = System.currentTimeMillis();
        this.message = message;
    }

}
