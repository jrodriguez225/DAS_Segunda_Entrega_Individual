package com.example.primeraentregaindividual;

import java.util.ArrayList;

// Clase que representa un chat cualquiera
public class Chat {

    private Usuario emisor; // Emisor del chat (usuario actual)
    private Usuario receptor; // Receptor del chat (otro usuario)
    private ArrayList<Mensaje> lista; // Lista de mensajes que forman el chat

    // Constructora del chat en la que se inicializan los atributos
    public Chat(Usuario pemisor, Usuario preceptor) {
        emisor = pemisor;
        receptor = preceptor;
        lista = new ArrayList<Mensaje>();
    }

    // Método que devuelve el emisor
    public Usuario getEmisor() {
        return emisor;
    }

    // Método que devuelve el receptor
    public Usuario getReceptor() {
        return receptor;
    }

    // Método que devuelve la lista de mensajes
    public ArrayList<Mensaje> getLista() {
        return lista;
    }
}
