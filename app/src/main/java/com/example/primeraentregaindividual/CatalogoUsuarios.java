package com.example.primeraentregaindividual;

import java.util.HashMap;

// Catálogo de usuarios
public class CatalogoUsuarios {

    private static CatalogoUsuarios catalogoUsuarios; // La clase es una MAE (instancia única) por lo que se hace referencia a la clase mediante un atributo estático
    private HashMap<String, Usuario> lista; // Lista que contiene los usuarios
    private Usuario usuarioActual; // Usuario que inicia sesión

    // Constructora que inicializa la lista de usuarios
    private CatalogoUsuarios() {
        lista = new HashMap<String, Usuario>();
    }

    // Método estático que devuelve la instancia de la clase
    public static CatalogoUsuarios getCatalogoUsuarios() {
        if(catalogoUsuarios ==null) {
            catalogoUsuarios = new CatalogoUsuarios();
        }
        return catalogoUsuarios;
    }

    // Método que devuelve la lista de usuarios
    public HashMap<String, Usuario> getLista() {
        return lista;
    }

    // Método que establece el usuario actual
    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    // Método que devuelve el usuario actual
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
