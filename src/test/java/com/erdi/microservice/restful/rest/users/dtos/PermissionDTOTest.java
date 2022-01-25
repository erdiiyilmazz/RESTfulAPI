package com.erdi.microservice.restful.rest.users.dtos;

import org.junit.Test;

import com.erdi.microservice.restful.rest.users.dtos.PermissionDTO;
import com.erdi.microservice.restful.rest.users.entities.Permission;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PermissionDTOTest {

    @Test
    public void testPermissionDTOConstructor1() {
        PermissionDTO permissionDTO = new PermissionDTO();

        assertEquals(null, permissionDTO.getId());
        assertEquals(null, permissionDTO.getPermission());
    }

    @Test
    public void testPermissionDTOConstructor2() {
        Permission permission = new Permission(1L, "Browse website");

        PermissionDTO permissionDTO = new PermissionDTO(permission);

        assertEquals(permission.getId(), permissionDTO.getId());
        assertEquals(permission.getPermission(), permissionDTO.getPermission());
        assertTrue(permissionDTO.isEnabled());
    }

}
