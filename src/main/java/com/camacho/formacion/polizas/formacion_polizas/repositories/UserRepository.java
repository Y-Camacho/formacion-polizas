package com.camacho.formacion.polizas.formacion_polizas.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.camacho.formacion.polizas.formacion_polizas.entities.User;


public interface UserRepository extends MongoRepository<User, String> {

    
}
