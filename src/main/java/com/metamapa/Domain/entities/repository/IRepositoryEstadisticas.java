package com.metamapa.Domain.entities.repository;

import com.metamapa.Domain.entities.InterfaceEstadistica;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IRepositoryEstadisticas extends MongoRepository<InterfaceEstadistica,String> {
}
