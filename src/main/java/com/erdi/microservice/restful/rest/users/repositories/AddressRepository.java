package com.erdi.microservice.restful.rest.users.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.erdi.microservice.restful.rest.users.entities.Address;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

}
