package com.metamapa.Domain.entities.repository;

import com.metamapa.Config.EstadisticaUpdateMarker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryEstadisticaUpdate extends MongoRepository<EstadisticaUpdateMarker, String> { }

