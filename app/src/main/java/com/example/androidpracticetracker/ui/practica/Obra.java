package com.example.androidpracticetracker.ui.practica;

public class Obra {
    private String autor;
    private String nombre;
    private String etiquetas;

    public Obra(String autor, String nombre, String etiquetas) {
        this.autor = autor;
        this.nombre = nombre;
        this.etiquetas = etiquetas;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }

    @Override
    public String toString() {
        return autor + " " + nombre + " " + etiquetas;
    }
}
