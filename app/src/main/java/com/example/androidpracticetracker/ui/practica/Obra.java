package com.example.androidpracticetracker.ui.practica;

import android.os.Parcel;
import android.os.Parcelable;

public class Obra implements Parcelable {
    private String autor;
    private String nombre;
    private String etiquetas;

    public Obra(String autor, String nombre, String etiquetas) {
        this.autor = autor;
        this.nombre = nombre;
        this.etiquetas = etiquetas;
    }

    protected Obra(Parcel in) {
        autor = in.readString();
        nombre = in.readString();
        etiquetas = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(autor);
        parcel.writeString(nombre);
        parcel.writeString(etiquetas);
    }

    @Override
    public String toString() {
        return autor + " " + nombre + " " + etiquetas;
    }
}
