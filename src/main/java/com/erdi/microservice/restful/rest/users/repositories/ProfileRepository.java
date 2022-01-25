package com.erdi.microservice.restful.rest.users.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.erdi.microservice.restful.rest.users.entities.Profile;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {

    Optional<Profile> findByRole(String profile);

    @Query(value = "select count(*) from users_profiles where profile_id = ?1", nativeQuery = true)
    Long countRoleUsage(Long profileId);

}
