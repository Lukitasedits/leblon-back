package com.leblon.app.repositories;


import com.leblon.app.models.FiltroModel;
import com.leblon.app.models.PeliculaModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FiltrosRepository extends CrudRepository<FiltroModel, Long> {
    public abstract Optional<FiltroModel> findAllByNombre(String nombre);
    public abstract boolean existsByNombre(String nombre);
    public abstract void deleteByNombre(String nombre);
}
