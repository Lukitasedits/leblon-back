package com.leblon.app.services;

import com.leblon.app.Exceptions.MovieHasNotFoundException;
import com.leblon.app.repositories.PeliculasRepository;
import com.leblon.app.models.PeliculaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PeliculasService {

    @Autowired
    PeliculasRepository peliculasRepository;

    public ArrayList<PeliculaModel> buscar(String busque){
        ArrayList<PeliculaModel> peliculas = obtenerPeliculas();

        peliculas = filtrarPeliculas(peliculas, busque);
        Comparator<PeliculaModel> comparator = new Comparator<PeliculaModel>() {
            @Override
            public int compare(PeliculaModel p1, PeliculaModel p2) {
                return coincidencia(p2.getNombre().toLowerCase(), busque.toLowerCase()).compareTo(coincidencia(p1.getNombre().toLowerCase(), busque.toLowerCase()));
            }
        };
        Collections.sort(peliculas, comparator);
        return peliculas;
    }

    public static ArrayList<PeliculaModel> filtrarPeliculas(ArrayList<PeliculaModel> peliculas, String busque){
        return peliculas.stream().parallel().filter(p -> {
            String palabra = p.getNombre().toLowerCase();
            String busqueda = busque.toLowerCase();
            if(palabra.contains(busqueda)) {
                return true;
            }

            return peliculaContieneBusqueda(palabra, busqueda);
        }).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public static boolean peliculaContieneBusqueda(String nombrePelicula, String busqueda){
        for (int i = 0; i < nombrePelicula.length(); i++) {
            if(busqueda.length() > 0 && busqueda.charAt(0) == nombrePelicula.charAt(i)){
                if(busqueda.length() > 1){
                    busqueda = busqueda.substring(1);
                } else {
                    return true;
                }
            }
        }
        return  false;
    }

    public static Integer coincidencia(String pelicula, String busqueda){
        int coincidencia = 1;
        int contador = 1;
        if(!pelicula.contains(busqueda)){
            coincidencia = 0;
        } else {
            for(int i = 0; i < pelicula.length()-1; i++) {
                Character charActual = pelicula.charAt(i);
                Character nextChar = pelicula.charAt(i + 1);
                if (busqueda.contains(charActual.toString())) { //si la busqueda contiene al caracter actual...
                    if (busqueda.contains("" + charActual + nextChar)) {   //si la busqueda contiene el caracter actual mas el siguiente...
                        contador++;
                    } else if (contador > coincidencia) {
                        coincidencia = contador;
                        contador = 1;
                    }
                }
            }
        }
        return coincidencia;
    }

    @Transactional(readOnly = true)
    public ArrayList<PeliculaModel> obtenerPeliculas(){
        return (ArrayList<PeliculaModel>)peliculasRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PeliculaModel obtenerPorNombre(String nombre){
        try {
            return peliculasRepository.findAllByNombre(nombre).get();
        } catch (NoSuchElementException e){
            throw new MovieHasNotFoundException("La película " + nombre + " no ha sido encontrada.");
        }
    }

    @Transactional
    public PeliculaModel guardarPelicula(PeliculaModel newPelicula){
        return peliculasRepository.save(newPelicula);
    }

    @Transactional
    public boolean guardarPeliculas(ArrayList<PeliculaModel> peliculas){
        try {
            peliculasRepository.saveAll((Iterable<PeliculaModel>) peliculas);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Transactional
    public PeliculaModel actualizarPelicula(PeliculaModel pelicula){
        try{
            PeliculaModel newPelicula = obtenerPorIndice(pelicula.getIndice());
            newPelicula.setId(pelicula.getId());
            newPelicula.setImagen(pelicula.getImagen());
            newPelicula.setNombre(pelicula.getNombre());
            peliculasRepository.save(newPelicula);
            return newPelicula;
        }catch (NoSuchElementException e){
            throw new MovieHasNotFoundException("La película con indice " + pelicula.getIndice() + " no ha sido encontrada.");
        }
    }

    @Transactional(readOnly = true)
    public PeliculaModel obtenerPorIndice(Long indice){
        try {
            return peliculasRepository.findByIndice(indice).get();
        } catch (NoSuchElementException e){
            throw new MovieHasNotFoundException("La película con indice " + indice + " no ha sido encontrada.");
        }
    }

    @Transactional(readOnly = true)
    public PeliculaModel obtenerPorId(String id){
        try {
            return peliculasRepository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new MovieHasNotFoundException("La película con id " + id + " no ha sido encontrada.");
        }
    }

    public boolean existByIndice(Long indice){
        return  peliculasRepository.existsByIndice(indice);
    }

    @Transactional
    public void eliminarPelicula(Long indice){
        try{
            peliculasRepository.deleteByIndice(indice);
        }catch (Exception err){
            throw new MovieHasNotFoundException("La película con indice " + indice + " no ha sido encontrada.");
        }
    }

    public void eliminarPeliculaPorId(String id){
        try{
            peliculasRepository.deleteById(id);
        }catch (Exception err){
            throw new MovieHasNotFoundException("La película con el id " + id + " no ha sido encontrada.");
        }
    }
}
