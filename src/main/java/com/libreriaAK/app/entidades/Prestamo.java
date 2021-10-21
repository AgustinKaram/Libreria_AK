package com.libreriaAK.app.entidades;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Prestamo {
    
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy ="uuid2")
    private String id;
    
	@Temporal(TemporalType.DATE)
    private Date fecha;
    @Temporal(TemporalType.DATE)
    private Date devolucion; 
    @ManyToMany
    private List<Libro> libros;
    @OneToOne
    private Cliente cliente;

    public Prestamo(String id, Date fecha, Date devolucion, List<Libro> libros, Cliente cliente) {
        this.id = id;
        this.fecha = fecha;
        this.devolucion = devolucion;
        this.libros = libros;
        this.cliente = cliente;
    }

    public Prestamo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha() {
        Calendar hoy = Calendar.getInstance();
    	this.fecha = hoy.getTime();
    }

    public Date getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(Date devolucion) {
        this.devolucion = devolucion;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

	@Override
	public String toString() {
		String imprimir = "";
		for(Libro li : libros) {
			imprimir.concat(" "+li.getIsbn()+" "+li.getTitulo()+" ;");
		}
		return imprimir;
	}
    
    
       
}
