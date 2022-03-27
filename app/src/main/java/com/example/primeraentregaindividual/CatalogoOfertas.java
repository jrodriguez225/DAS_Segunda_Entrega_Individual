package com.example.primeraentregaindividual;

import java.util.HashMap;

// Catálogo de ofertas
public class CatalogoOfertas {

    private static CatalogoOfertas catalogoOfertas; // La clase es una MAE (instancia única) por lo que se hace referencia a la clase mediante un atributo estático
    private HashMap<String, Oferta> lista; // Lista que contiene las ofertas

    // Constructora que inicializa la lista de ofertas
    private CatalogoOfertas() {
        lista = new HashMap<String, Oferta>();
    }

    // Método estático que devuelve la instancia de la clase
    public static CatalogoOfertas getCatalogoOfertas() {
        if(catalogoOfertas ==null) {
            catalogoOfertas = new CatalogoOfertas();
        }
        return catalogoOfertas;
    }

    // Método que devuelve la lista de ofertas
    public HashMap<String, Oferta> getLista() {
        return lista;
    }
}
