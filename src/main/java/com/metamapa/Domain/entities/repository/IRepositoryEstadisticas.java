package com.metamapa.Domain.entities.repository;

import com.metamapa.Domain.entities.InterfaceEstadistica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepositoryEstadisticas extends MongoRepository<InterfaceEstadistica,String> {
}
