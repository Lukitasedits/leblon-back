package com.leblon.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "filtros")
public class FiltroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String nombre;

    @ManyToMany(fetch = FetchType.LAZY)
    //@JsonManagedReference
    private Set<PeliculaModel> peliculas = new HashSet<>();

    public FiltroModel() {
    }


    public FiltroModel(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<PeliculaModel> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(Set<PeliculaModel> peliculas) {
        this.peliculas = peliculas;
    }

    public void addPelicula(PeliculaModel pelicula){
        this.peliculas.add(pelicula);
    }

    public void removePelicula(Long indice){
        PeliculaModel pelicula = this.peliculas.stream().filter(p -> p.getIndice() == indice).findFirst().get();
        this.peliculas.remove(pelicula);
    }

    public boolean peliculasHasPelicula(Long indice){
        boolean retorno = this.peliculas.stream().anyMatch(p -> p.getIndice().equals(indice));
        return retorno;
    }
}
