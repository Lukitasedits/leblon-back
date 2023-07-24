package com.leblon.app.controllers;

import com.leblon.app.Exceptions.MovieHasNotFoundException;
import com.leblon.app.models.FiltroModel;
import com.leblon.app.models.FiltroPelicula;
import com.leblon.app.models.PeliculaModel;
import com.leblon.app.services.FiltrosService;
import com.leblon.app.services.PeliculasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:4200/"})
@RequestMapping("/filtros")
public class FiltroController {

    @Autowired
    FiltrosService filtrosService;

    @Autowired
    PeliculasService peliculasService;


    @GetMapping()
    public ResponseEntity<?> obtenerFiltros(){
        Map<String, Object> response = new HashMap<>();
        try{
            return new ResponseEntity<ArrayList<FiltroModel>>(filtrosService.obtenerFiltros(), HttpStatus.OK);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al intentar obtener los filtros.");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(path ="/peliculas/{filtroId}")
    public ResponseEntity<?> obtenerPeliculas(@PathVariable("filtroId") Long filtroId){
        Map<String, Object> response = new HashMap<>();
        try{

            return new ResponseEntity<ArrayList<PeliculaModel>>(filtrosService.obtenerPeliculas(filtroId), HttpStatus.OK);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al intentar obtener las peliculas del filtro con id " + filtroId + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            response.put("mensaje", "No se pudo obtener las peliculas");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> guardarFiltro(@RequestBody FiltroModel filtro){
        Map<String, Object> response = new HashMap<>();
        try{
            FiltroModel newFiltro = this.filtrosService.guardarFiltro(filtro);
            return new ResponseEntity<FiltroModel>(newFiltro, HttpStatus.CREATED);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al intentar guardar el filtro " + filtro.getNombre() + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Error e){
            response.put("mensaje", "Ya existe un filtro con el nombre " + filtro.getNombre() + ".");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    //@PutMapping(value="/update", consumes = "application/json")
    @PutMapping("/update")
    public ResponseEntity<?> updateFiltro(@RequestBody FiltroModel filtro){
        Map<String, Object> response = new HashMap<>();
        try {
            return new ResponseEntity<FiltroModel>(filtrosService.actualizarFiltro(filtro), HttpStatus.OK);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al intentar actualizar " + filtro.getNombre());
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Error e){
            response.put("mensaje", "No se ha encontrado el filtro.");
            response.put("error", e.getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/add/{filtroId}/{peliculaIndice}")
    public ResponseEntity<?> addPeliculaAFiltro(@PathVariable("filtroId") Long filtroId, @PathVariable("peliculaIndice") Long peliculaIndice){
        Map<String, Object> response = new HashMap<>();
        try {
            return new ResponseEntity<FiltroModel>(filtrosService.agregarPeliculaAFiltro(filtroId, peliculaIndice), HttpStatus.OK);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al intentar añadir la pelicula con indice " + peliculaIndice + " al filtro con id " + filtroId);
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            response.put("mensaje", "Error al intentar añadir la pelicula con indice " + peliculaIndice + " al filtro con id " + filtroId);
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{filtroId}/{peliculaIndice}")
    public ResponseEntity<?> deletePeliculaAFiltro(@PathVariable("filtroId") Long filtroId, @PathVariable("peliculaIndice") Long peliculaIndice){
        Map<String, Object> response = new HashMap<>();
        try {
            filtrosService.eliminarPeliculaAFiltro(filtroId, peliculaIndice);
            response.put("mensaje", "Se ha quitado la pelicula de indice " + peliculaIndice + " al filtro con id " + filtroId);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al intentar añadir la pelicula con indice " + peliculaIndice + " al filtro con id " + filtroId);
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            response.put("mensaje", "No se ha encontrado la pelicula o el filtro");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/id/{id}")
    public ResponseEntity<?> obtenerFiltroPorId(@PathVariable("id") Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            return new ResponseEntity<FiltroModel>(filtrosService.obtenerPorId(id), HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al intentar obtener el filtro con id " + id);
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Error e){
            response.put("mensaje", "No se ha encontrado el filtro con id " + id + ".");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/nombre/{nombre}")
    public ResponseEntity<?> obtenerFiltroPorNombre(@PathVariable("nombre") String nombre){
        Map<String, Object> response = new HashMap<>();
        try{
            return new ResponseEntity<FiltroModel>(this.filtrosService.obtenerPorNombre(nombre), HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al intentar obtener el filtro con nombre " + nombre);
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Error e){
            response.put("mensaje", "No se ha encontrado la filtro con el nombre \"" + nombre + "\".");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/id/{id}")
    public ResponseEntity<?> eliminarPorId(@PathVariable("id") Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.filtrosService.eliminarFiltro(id);
            response.put("mensaje", "Se ha eliminado el filtro con id " + id + " con éxito. ");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al intentar eliminar el filtro con el id " + id + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Error e){
            response.put("mensaje", "No se ha encontrado el filtro con el id " + id + ".");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/nombre/{nombre}")
    public ResponseEntity<?> eliminarPorNombre(@PathVariable("nombre") String nombre){
        Map<String, Object> response = new HashMap<>();
        try{
            this.filtrosService.eliminarFiltroPorNombre(nombre);
            response.put("mensaje", "Se ha eliminado el filtro " + nombre + " con éxito. ");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al intentar eliminar el filtro " + nombre + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Error e){
            response.put("mensaje", "No se ha encontrado el filtro con el " + nombre + ".");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }



}
