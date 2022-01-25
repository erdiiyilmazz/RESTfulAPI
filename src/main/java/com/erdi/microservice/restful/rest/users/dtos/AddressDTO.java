package com.erdi.microservice.restful.rest.users.dtos;

import lombok.Data;

import java.io.Serializable;

import com.erdi.microservice.restful.rest.users.entities.Address;

@Data
public class AddressDTO implements Serializable {

    public AddressDTO() {
        // empty constructor
    }

    public AddressDTO(Address address) {
        if (address != null) {
            this.address = address.getAddress();
            this.city = address.getCity();
            this.country = address.getCountry();
            this.zipCode = address.getZipCode();
        }
    }

    private String address;
    private String city;
    private String country;
    private String zipCode;

}
