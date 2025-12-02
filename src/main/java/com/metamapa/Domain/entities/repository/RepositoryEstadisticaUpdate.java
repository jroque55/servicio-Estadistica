package com.metamapa.Domain.entities.repository;

import com.metamapa.Config.EstadisticaUpdateMarker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositoryEstadisticaUpdate extends MongoRepository<EstadisticaUpdateMarker, String> { }

