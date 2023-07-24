package com.leblon.app.controllers;

import com.leblon.app.Exceptions.MovieHasNotFoundException;
import com.leblon.app.models.PeliculaModel;
import com.leblon.app.services.PeliculasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@CrossOrigin(origins = {"http://localhost:4200/catalogo"})
@RequestMapping("/peliculas")
public class PeliculasController {
    
    @Autowired
    PeliculasService peliculasService;

    @GetMapping()
    public ResponseEntity<?> obtenerPeliculas(){
        Map<String, Object> response = new HashMap<>();
        try{
            return new ResponseEntity<ArrayList<PeliculaModel>>(peliculasService.obtenerPeliculas(), HttpStatus.OK);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al intentar obtener las peliculas.");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> guardarPelicula(@RequestBody PeliculaModel pelicula){
        Map<String, Object> response = new HashMap<>();
        try{
            PeliculaModel newPelicula = this.peliculasService.guardarPelicula(pelicula);
            return new ResponseEntity<PeliculaModel>(newPelicula, HttpStatus.CREATED);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al intentar guardar la pelicula " + pelicula.getNombre() + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveall")
    public ResponseEntity<?> guardarPeliculas(@RequestBody ArrayList<PeliculaModel> peliculas){
        Map<String, Object> response = new HashMap<>();
        try{
            this.peliculasService.guardarPeliculas(peliculas);
            return new ResponseEntity<String>("Se ha subido con éxito.", HttpStatus.OK);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al intentar guardar las películas solicitadas.");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePelicula(@RequestBody PeliculaModel pelicula){
        Map<String, Object> response = new HashMap<>();
        try {
            return new ResponseEntity<PeliculaModel>(peliculasService.actualizarPelicula(pelicula), HttpStatus.OK);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al intentar actualizar " + pelicula.getNombre());
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (MovieHasNotFoundException e){
            response.put("mensaje", "No se ha encontrado la película.");
            response.put("error", e.getMessage());
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/indice/{indice}")
    public ResponseEntity<?> obtenerPeliculaPorIndice(@PathVariable("indice") Long indice){
        Map<String, Object> response = new HashMap<>();
        try {
            return new ResponseEntity<PeliculaModel>(this.peliculasService.obtenerPorIndice(indice), HttpStatus.OK);
        }catch (DataAccessException e) {
            response.put("mensaje", "Error al intentar obtener la pelicula con indice " + indice);
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (MovieHasNotFoundException e){
            response.put("mensaje", "No se ha encontrado la película.");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/id/{id}")
    public ResponseEntity<?> obtenerPeliculaPorId(@PathVariable("id") String id){
        Map<String, Object> response = new HashMap<>();
        try{
            return new ResponseEntity<PeliculaModel>(peliculasService.obtenerPorId(id), HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al intentar obtener la pelicula con id " + id);
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MovieHasNotFoundException e){
            response.put("mensaje", "No se ha encontrado la película con id " + id + ".");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/nombre/{nombre}")
    public ResponseEntity<?> obtenerPeliculaPorNombre(@PathVariable("nombre") String nombre){
        Map<String, Object> response = new HashMap<>();
        try{
            return new ResponseEntity<PeliculaModel>(this.peliculasService.obtenerPorNombre(nombre), HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al intentar obtener la pelicula con nombre " + nombre);
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MovieHasNotFoundException e){
            response.put("mensaje", "No se ha encontrado la película con el nombre \"" + nombre + "\".");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/buscar/{buscar}")
    public ResponseEntity<?> obtenerPeliculaPorBusqueda(@PathVariable("buscar") String buscar){
        Map<String, Object> response = new HashMap<>();
        try{
            return new ResponseEntity<ArrayList<PeliculaModel>>(peliculasService.buscar(buscar), HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al intentar buscar peliculas con en nombre " + buscar + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MovieHasNotFoundException e){
            response.put("mensaje", "No se ha encontrado la películas con el nombre \"" + buscar + "\".");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/page/{page}/{buscar}/{incremento}")
    public ResponseEntity<?> obtenerPeliculasPorPaginas(@PathVariable("page") int page, @PathVariable(value = "buscar") String buscar, @PathVariable("incremento") int incremento){
        Map<String, Object> response = new HashMap<>();
        try {
            int desde = incremento * page;
            int hasta = desde + incremento;
            ArrayList<PeliculaModel> peliculas = null;
            if(buscar.equals("*") || buscar.isBlank() || buscar.isEmpty() || buscar == null){
                peliculas = peliculasService.obtenerPeliculas().stream().filter(p -> (desde <= p.getIndice() && p.getIndice() < hasta)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            } else {
                AtomicInteger i = new AtomicInteger(0);
                peliculas = peliculasService.buscar(buscar);
                peliculas = peliculas.stream().filter(p -> {
                    return (i.get() >= desde & i.getAndIncrement() < hasta);
                })
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            }
            return new ResponseEntity<ArrayList<PeliculaModel>>(
                    peliculas,
                    HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al intentar obtener peliculas en la página " + page + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path="/indice/{indice}")
    public ResponseEntity<?> eliminarPorIndice(@PathVariable("indice") Long indice){
        Map<String, Object> response = new HashMap<>();
        try{
            this.peliculasService.eliminarPelicula(indice);
            response.put("mensaje", "Se ha eliminado la pelicula con indice " + indice + ".");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al intentar eliminar la peícula con el indice " + indice + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MovieHasNotFoundException e){
            response.put("mensaje", "No se ha encontrado la películas con el indice " + indice + ".");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/id/{id}")
    public ResponseEntity<?> eliminarPorId(@PathVariable("id") String id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.peliculasService.eliminarPeliculaPorId(id);
            return new ResponseEntity<String>("Se ha eliminado la pelicula con id " + id + " con éxito. ", HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al intentar eliminar la película con el id " + id + ".");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MovieHasNotFoundException e){
            response.put("mensaje", "No se ha encontrado la películas con el id " + id + ".");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
