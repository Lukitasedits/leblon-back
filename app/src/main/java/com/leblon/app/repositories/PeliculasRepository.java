package com.leblon.app.repositories;

import com.leblon.app.models.PeliculaModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeliculasRepository extends CrudRepository<PeliculaModel, Long> {
    public abstract Optional<PeliculaModel> findById(String id);
    public abstract Optional<PeliculaModel> findByIndice(Long indice);
    public abstract Optional<PeliculaModel> findAllByNombre(String nombre);
    public abstract  boolean existsByIndice(Long indice);
    public abstract void deleteByIndice(Long indice);
    public abstract boolean deleteById(String id);
}
