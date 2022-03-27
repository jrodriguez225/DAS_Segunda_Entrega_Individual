package com.example.primeraentregaindividual;

import java.util.ArrayList;

// Clase abstracta que representa una oferta cualquiera
public abstract class Oferta {

    private String nombre; // Nombre de la oferta
    private String descripcion; // Descripción de la oferta
    private Usuario usuario; // Usuario que genera la oferta
    private ArrayList<Usuario> demandantes; // Lista de usuarios que tienen la oferta entre sus favoritos

    // Constructora que inicializa los atributos de la clase
    public Oferta(String pnombre, String pdescripcion, Usuario pusuario) {
        nombre = pnombre;
        descripcion = pdescripcion;
        usuario = pusuario;
        demandantes = new ArrayList<Usuario>();
    }

    // Método que devuelve el nombre de la oferta
    public String getNombre() {
        return nombre;
    }

    // Método que devuelve la descripción de la oferta
    public String getDescripcion() {
        return descripcion;
    }

    // Método que devuelve el usuario que genera la oferta
    public Usuario getUsuario() {
        return usuario;
    }

    // Método que devuelve la lista de usuarios que tienen la oferta entre sus favoritos
    public ArrayList<Usuario> getDemandantes() {
        return demandantes;
    }

    // Método que establece la descripción de la oferta
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
