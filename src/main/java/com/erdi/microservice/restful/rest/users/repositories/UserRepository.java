package com.erdi.microservice.restful.rest.users.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erdi.microservice.restful.rest.users.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT a FROM User a WHERE a.contact.email = :email")
    User findByEmail(@Param("email") String email);

    User findByUsername(String username);

}
