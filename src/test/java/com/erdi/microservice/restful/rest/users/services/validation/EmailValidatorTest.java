package com.erdi.microservice.restful.rest.users.services.validation;

import org.junit.Before;
import org.junit.Test;

import com.erdi.microservice.restful.rest.users.exceptions.InvalidUserDataException;
import com.erdi.microservice.restful.rest.users.services.validation.EmailValidator;

public class EmailValidatorTest {

    private EmailValidator emailValidator;

    @Before
    public void initTest() {
        emailValidator = new EmailValidator();
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_null_email_when_checkEmail_throw_InvalidUserDataException() {
        String email = null;
        emailValidator.checkEmail(email);
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_empty_email_when_checkEmail_throw_InvalidUserDataException() {
        String email = "";
        emailValidator.checkEmail(email);
    }

    @Test(expected = InvalidUserDataException.class)
    public void given_invalid_email_when_checkEmail_throw_InvalidUserDataException() {
        String email = "@gmail.com";
        emailValidator.checkEmail(email);
    }

    @Test
    public void given_valid_email_when_checkEmail_OK() {
        String email = "testEmail@gmail.com";
        emailValidator.checkEmail(email);
    }

}
