package com.leblon.app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "peliculas")
public class PeliculaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long indice;

    private String nombre;
    private String id;
    private String imagen;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<FiltroModel> filtros = new HashSet<>();

    public Long getIndice() {
        return indice;
    }

    public void setIndice(Long indice) {
        this.indice = indice;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Set<FiltroModel> getFiltros() {
        return filtros;
    }

    public void setFiltros(Set<FiltroModel> filtros) {
        this.filtros = filtros;
    }
}