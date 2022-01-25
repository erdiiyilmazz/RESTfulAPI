package com.erdi.microservice.restful.rest.users.dtos;

import org.junit.Assert;
import org.junit.Test;

import com.erdi.microservice.restful.rest.users.dtos.UserListDTO;

public class UserListDTOTest {

    @Test
    public void userListDTOTest() {
        UserListDTO userListDTO = new UserListDTO();

        Assert.assertNotNull(userListDTO.getUserList().size());
        Assert.assertEquals(0, userListDTO.getUserList().size());
    }

}
