package com.leblon.app.repositories;

import com.leblon.app.models.FiltroModel;
import com.leblon.app.models.FiltroPelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FiltrosPeliculasRepository extends JpaRepository<FiltroPelicula, Long> {
    public abstract Optional<FiltroPelicula> findByFiltroIdAndPelicula_Indice(Long filtroId, Long peliculaIndice);
}
