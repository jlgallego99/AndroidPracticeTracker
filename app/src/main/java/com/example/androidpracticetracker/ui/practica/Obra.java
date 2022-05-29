package com.example.androidpracticetracker.ui.practica;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.DayOfWeek;

public class Obra implements Parcelable {
    private String autor;
    private String nombre;
    private String etiquetas;
    private float tiempoEstudiado; // Tiempo estudiado en esta obra (en segundos)
    private int ultimoEstudio;  // Día de la semana en el cual se realizó el último estudio

    public Obra(String autor, String nombre, String etiquetas) {
        this.autor = autor;
        this.nombre = nombre;
        this.etiquetas = etiquetas;
        this.tiempoEstudiado = 0;
    }

    protected Obra(Parcel in) {
        autor = in.readString();
        nombre = in.readString();
        etiquetas = in.readString();
        tiempoEstudiado = 0;
    }

    public static final Creator<Obra> CREATOR = new Creator<Obra>() {
        @Override
        public Obra createFromParcel(Parcel in) {
            return new Obra(in);
        }

        @Override
        public Obra[] newArray(int size) {
            return new Obra[size];
        }
    };

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

    public float getTiempoEstudiado() {
        return tiempoEstudiado;
    }

    public void setTiempoEstudiado(float tiempoEstudiado) {
        this.tiempoEstudiado = tiempoEstudiado;
    }

    public int getUltimoEstudio() {
        return ultimoEstudio;
    }

    public void setUltimoEstudio(int ultimoEstudio) {
        this.ultimoEstudio = ultimoEstudio;
    }

    public void addTiempoEstudiado(float tiempo) {
        this.tiempoEstudiado += tiempo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(autor);
        parcel.writeString(nombre);
        parcel.writeString(etiquetas);
        parcel.writeFloat(tiempoEstudiado);
    }

    @Override
    public String toString() {
        return nombre + " (" + tiempoEstudiado + " segundos)";
    }
}
