package com.erdi.microservice.restful.rest.users.dtos;

import org.junit.Test;

import com.erdi.microservice.restful.rest.users.dtos.AddressDTO;
import com.erdi.microservice.restful.rest.users.entities.Address;

import static org.junit.Assert.assertEquals;

public class AddressDTOTest {

    @Test
    public void testAddressDTOConstructor1() {
        AddressDTO addressDTO = new AddressDTO();
        assertEquals(null, addressDTO.getCity());
        assertEquals(null, addressDTO.getCountry());
        assertEquals(null, addressDTO.getZipCode());
        assertEquals(null, addressDTO.getAddress());
    }

    @Test
    public void testAddressDTOConstructor2() {
        Address address = new Address();
        address.setCity("Trieste");
        address.setCountry("Turkey");
        address.setZipCode("34100");

        AddressDTO addressDTO = new AddressDTO(address);
        assertEquals("Trieste", addressDTO.getCity());
        assertEquals("Turkey", addressDTO.getCountry());
        assertEquals("34100", addressDTO.getZipCode());
    }

}
