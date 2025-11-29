package com.metamapa.Domain.entities.repository;

import com.metamapa.Domain.entities.InterfaceEstadistica;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IRepositoryEstadisticas extends MongoRepository<InterfaceEstadistica,String> {
}
/*
Se deberia agregar lo siguiente
spring.data.mongodb.uri=mongodb://localhost:27017/miBase
spring.data.mongodb.database=miBase
*/