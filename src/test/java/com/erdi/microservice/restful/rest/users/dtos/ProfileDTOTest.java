package com.erdi.microservice.restful.rest.users.dtos;

import org.junit.Test;

import com.erdi.microservice.restful.rest.users.dtos.ProfileDTO;
import com.erdi.microservice.restful.rest.users.entities.Profile;

import static org.junit.Assert.*;

public class ProfileDTOTest {

    @Test
    public void testRoleDTOConstructor1() {
        Profile profile = new Profile(1L, "USER");

        ProfileDTO profileDTO = new ProfileDTO(profile);

        assertEquals(profile.getId(), profileDTO.getId());
        assertEquals(profile.getProfile(), profileDTO.getProfile());
    }

    @Test
    public void testRoleDTOConstructor2() {
        ProfileDTO profileDTO = new ProfileDTO(1L, "USER");

        assertEquals(Long.valueOf(1L), profileDTO.getId());
        assertEquals("USER", profileDTO.getProfile());
    }

    @Test
    public void testEquals() {
        ProfileDTO profileDTO = new ProfileDTO(1L, "USER");
        ProfileDTO profileDTO2 = new ProfileDTO(1L, "USER");

        assertTrue(profileDTO.equals(profileDTO));
        assertFalse(profileDTO.equals("WRONG"));
        assertTrue(profileDTO.equals(profileDTO2));
    }

}
