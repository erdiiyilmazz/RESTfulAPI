package com.erdi.microservice.restful.rest.users.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.erdi.microservice.restful.rest.users.entities.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {

}
