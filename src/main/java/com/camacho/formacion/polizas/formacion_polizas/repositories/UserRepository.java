package com.camacho.formacion.polizas.formacion_polizas.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.camacho.formacion.polizas.formacion_polizas.entities.User;


public interface UserRepository extends MongoRepository<User, String> {

    @Query("{'lastName': {$regex: /?0/i}}")
    List<User> findLikeLastName(String lastname);
}
