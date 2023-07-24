package com.leblon.app.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class FiltroPelicula implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filtro_id")
    private FiltroModel filtro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pelicula_indice")
    private PeliculaModel pelicula;

    public FiltroPelicula() {
    }

    public FiltroPelicula(FiltroModel filtro, PeliculaModel pelicula) {
        this.filtro = filtro;
        this.pelicula = pelicula;
    }

    @PreRemove
    private void removeRelation() {
        filtro.getPeliculas().remove(this);
        pelicula.getFiltros().remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PeliculaModel getPelicula() {
        return pelicula;
    }

    public void setPelicula(PeliculaModel pelicula) {
        this.pelicula = pelicula;
    }

    public FiltroModel getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroModel filtro) {
        this.filtro = filtro;
    }
}
