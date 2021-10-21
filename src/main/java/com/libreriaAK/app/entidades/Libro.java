package com.libreriaAK.app.entidades;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Libro {
    
    @Id
    private Long  isbn;
    
    private String titulo;
    private Integer anio;
    private Integer ejemplares;
    private Integer prestados;
    
    @OneToOne(cascade=CascadeType.PERSIST) 
    private Autor autor;
    @OneToOne(cascade=CascadeType.PERSIST)
    private Editorial editorial;
    
    public Libro(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer prestados, Autor autor, Editorial editorial) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.anio = anio;
        this.ejemplares = ejemplares;
        this.prestados = prestados;
        this.autor = autor;
        this.editorial = editorial;
    }

    public Libro() {
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(Integer ejemplares) {
        this.ejemplares = ejemplares;
    }

    public Integer getPrestados() {
        return prestados;
    }

    public void setPrestados(Integer prestados) {
        this.prestados = prestados;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

}