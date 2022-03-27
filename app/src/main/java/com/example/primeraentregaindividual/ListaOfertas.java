package com.example.primeraentregaindividual;

import java.util.ArrayList;
import java.util.Collection;

// Clase que hace referencia a una lista de ofertas cualquiera
public class ListaOfertas {

    private ArrayList<Oferta> lista; // Lista que contiene las ofertas

    // Constructora que inicializa la lista de ofertas
    public ListaOfertas() {
        lista = new ArrayList<Oferta>();
    }

    // Constructora que inicializa la lista de ofertas en base a otra lista
    public ListaOfertas(Collection<Oferta> plista) {
        lista = new ArrayList<Oferta>(plista);
    }

    // Método que devuelve la lista de ofertas
    public ArrayList<Oferta> getLista() {
        return lista;
    }

    // Método que filtra la lista y devuelve la oferta correspondiente a una posición concreta
    public Oferta filtrarOferta(String filtro, int i) {
        Oferta oferta = null;
        boolean enc = false;
        int i1 = 0;
        int cont = 0;
        if(filtro==null){
            oferta = lista.get(i);
        }
        else {
            if (filtro.equals("empleos")) {
                while(!enc) {
                    oferta = lista.get(cont);
                    if(oferta instanceof  Empleo) {
                        if(i1==i) {
                            enc = true;
                        }
                        else {
                            i1++;
                        }
                    }
                    cont++;
                }
            }
            else if (filtro.equals("servicios")) {
                while(!enc) {
                    oferta = lista.get(cont);
                    if(oferta instanceof  Servicio) {
                        if(i1==i) {
                            enc = true;
                        }
                        else {
                            i1++;
                        }
                    }
                    cont++;
                }
            }
        }
        return oferta;
    }

    // Método devuelve una lista de ofertas generada en base a aplicar un filtro a la lista original
    public ArrayList<Oferta> filtrarListaOfertas(String filtro) {
        ArrayList<Oferta> listaFiltrada = new ArrayList<Oferta>();
        ArrayList<Oferta> listaUsuario = CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual().getOfertas().getLista();
        if(filtro==null) {
            if(listaUsuario.isEmpty()) {
                listaFiltrada = lista;
            }
            else {
                for (int i = 0; i < lista.size(); i++) {
                    Oferta oferta = lista.get(i);
                    if (!listaUsuario.contains(oferta)) {
                        listaFiltrada.add(oferta);
                    }
                }
            }
        }
        else if (filtro.equals("empleos")) {
            for (int i=0; i<lista.size(); i++) {
                Oferta oferta = lista.get(i);
                if(oferta instanceof  Empleo && !listaUsuario.contains(oferta)) {
                    listaFiltrada.add(oferta);
                }
            }
        }
        else if (filtro.equals("servicios")) {
            for (int i=0; i<lista.size(); i++) {
                Oferta oferta = lista.get(i);
                if(oferta instanceof  Servicio && !listaUsuario.contains(oferta)) {
                    listaFiltrada.add(oferta);
                }
            }
        }
        return listaFiltrada;
    }
}
