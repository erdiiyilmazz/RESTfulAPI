package com.erdi.microservice.restful.rest.users.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.erdi.microservice.restful.rest.users.entities.Permission;
import com.erdi.microservice.restful.rest.users.repositories.PermissionRepository;
import com.erdi.microservice.restful.rest.users.services.PermissionService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @Autowired
    @InjectMocks
    private PermissionService permissionService;

    @Test
    public void calling_getPermissionList_then_return_list_of_permissions() {
        ArrayList<Permission> permissionArrayList = new ArrayList<>();

        Permission permission1 = new Permission(1L, "LOGIN");
        Permission permission2 = new Permission(2L, "VIEW_ROLE");

        permissionArrayList.add(permission1);
        permissionArrayList.add(permission2);

        given(permissionRepository.findAll()).willReturn(permissionArrayList);

        List<Permission> permissionList = (List<Permission>) permissionService.getPermissionList();

        assertNotNull(permissionList);

        assertEquals(2, permissionList.size());
        assertTrue(permissionList.contains(new Permission(1L, "LOGIN")));
        assertTrue(permissionList.contains(new Permission(2L, "VIEW_ROLE")));
    }

    // getPermissionById

    // getPermissionByKey

}
