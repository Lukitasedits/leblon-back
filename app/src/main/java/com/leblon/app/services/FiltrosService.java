package com.leblon.app.services;

import com.leblon.app.Exceptions.FilterHasNotFoundException;
import com.leblon.app.Exceptions.FilterHasThatMovie;
import com.leblon.app.Exceptions.MovieHasNotFoundException;
import com.leblon.app.models.FiltroModel;
import com.leblon.app.models.FiltroPelicula;
import com.leblon.app.models.PeliculaModel;
import com.leblon.app.repositories.FiltrosPeliculasRepository;
import com.leblon.app.repositories.FiltrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class FiltrosService {

    @Autowired
    FiltrosRepository filtrosRepository;

    @Autowired
    FiltrosPeliculasRepository filtrosPeliculasRepository;

    @Autowired
    PeliculasService peliculasService;

    public ArrayList<FiltroModel> obtenerFiltros(){
        return (ArrayList<FiltroModel>)filtrosRepository.findAll();
    }

    @Transactional(readOnly = true)
    public FiltroModel obtenerPorNombre(String nombre){
        try {
            return filtrosRepository.findAllByNombre(nombre).get();
        } catch (NoSuchElementException e){
            throw new Error("El filtro " + nombre + " no ha sido encontrado.");
        }
    }

    @Transactional(readOnly = true)
    public ArrayList<PeliculaModel> obtenerPeliculas(Long filtroId){
        FiltroModel filtro = filtrosRepository.findById(filtroId).get();
        Set<PeliculaModel> peliculaSet = filtro.getPeliculas();
        ArrayList<PeliculaModel> peliculas = new ArrayList<>(peliculaSet);
        return peliculas;
    }

    @Transactional
    public FiltroModel guardarFiltro(FiltroModel newFiltro){
        if(filtrosRepository.existsByNombre(newFiltro.getNombre())){
            throw new Error("El filtro \"" + newFiltro.getNombre() + "\" ya existe en la base de datos.");
        }
        return filtrosRepository.save(newFiltro);
    }

    @Transactional
    public FiltroModel actualizarFiltro(FiltroModel filtro){
        try{
            FiltroModel newFiltro = obtenerPorId(filtro.getId());
            newFiltro.setId(filtro.getId());
            newFiltro.setNombre(filtro.getNombre());
            newFiltro.setPeliculas(filtro.getPeliculas());
            filtrosRepository.save(newFiltro);
            return newFiltro;
        }catch (NoSuchElementException e){
            throw new Error("El filtro " + filtro.getNombre() + " no ha sido encontrado.");        }
    }

    @Transactional(readOnly = true)
    public FiltroModel obtenerPorId(Long id){
        try {
            return filtrosRepository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new Error("El filtro con id " + id + " no ha sido encontrado.");
        }
    }

    @Transactional
    public void eliminarFiltro(Long id){
        try{
            filtrosRepository.deleteById(id);
        }catch (Exception err){
            throw new Error("El filtro con id " + id + " no ha sido encontrada.");
        }
    }

    @Transactional
    public void eliminarFiltroPorNombre(String nombre){
        try{
            filtrosRepository.deleteByNombre(nombre);
        }catch (Exception err){
            throw new Error("El filtro " + nombre + " no ha sido encontrada.");
        }
    }

    public boolean existFiltroById(Long filtroId){
        return filtrosRepository.existsById(filtroId);
    }

    public FiltroModel agregarPeliculaAFiltro(Long filtroId, Long peliculaIndice){
        FiltroPelicula filtroPelicula = null;

        if(!filtrosRepository.existsById(filtroId)){
            throw new FilterHasNotFoundException("Filtro con id " + filtroId +  " no encontrado.");
        }
        if(!peliculasService.existByIndice(peliculaIndice)){
            throw new MovieHasNotFoundException("Pelicula con indice " + peliculaIndice + " no encontrada.");
        }

        if(filtrosPeliculasRepository.findByFiltroIdAndPelicula_Indice(filtroId, peliculaIndice).isPresent()){
            throw  new FilterHasThatMovie("El filtro con id " + filtroId + " ya tiene la pelicula " + peliculaIndice + ".");
        }
        FiltroModel newFiltro = obtenerPorId(filtroId);
        newFiltro.addPelicula(peliculasService.obtenerPorIndice(peliculaIndice));
        //filtroPelicula = new FiltroPelicula(obtenerPorId(filtroId), peliculasService.obtenerPorIndice(peliculaIndice));
        return filtrosRepository.save(newFiltro);
    }

    @Transactional
    public void eliminarPeliculaAFiltro(Long filtroId, Long peliculaIndice){
        if(!filtrosRepository.existsById(filtroId)){
            throw new FilterHasNotFoundException("Filtro con id " + filtroId +  " no encontrado.");
        }
        if(!peliculasService.existByIndice(peliculaIndice)){
            throw new MovieHasNotFoundException("Pelicula con indice " + peliculaIndice + " no encontrada.");
        }

        if(!filtrosRepository.findById(filtroId).get().peliculasHasPelicula(peliculaIndice)){
            throw new FilterHasThatMovie("El filtro con id " + filtroId + " no contiene la película de indice " + peliculaIndice);
        }

        FiltroModel newFiltro = filtrosRepository.findById(filtroId).get();
        newFiltro.removePelicula(peliculaIndice);

        filtrosRepository.save(newFiltro);
    }
}
