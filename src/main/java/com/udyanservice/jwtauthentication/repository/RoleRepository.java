package com.udyanservice.jwtauthentication.repository;

import java.util.Optional;

import com.udyanservice.jwtauthentication.model.RoleName;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.udyanservice.jwtauthentication.model.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(RoleName roleName);
}