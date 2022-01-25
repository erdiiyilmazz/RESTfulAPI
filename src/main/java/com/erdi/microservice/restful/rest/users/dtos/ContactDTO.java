package com.erdi.microservice.restful.rest.users.dtos;

import com.erdi.microservice.restful.rest.users.entities.Contact;

import lombok.Data;

@Data
public class ContactDTO implements java.io.Serializable {

    public ContactDTO() {
        // empty constructor
    }

    public ContactDTO(Contact contact) {
        if (contact != null) {
            this.email = contact.getEmail();
            this.phone = contact.getPhone();
            this.facebook = contact.getFacebook();
            this.linkedin = contact.getLinkedin();
            this.website = contact.getWebsite();
        }
    }

    private String email;
    private String phone;
    private String facebook;
    private String linkedin;
    private String website;
}
