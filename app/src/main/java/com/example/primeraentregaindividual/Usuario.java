package com.example.primeraentregaindividual;

import android.graphics.Bitmap;
import java.util.HashMap;

// Clase que representa un usuario cualquiera
public class Usuario {

    private int imagenDefault; // Imagen por defecto
    private Bitmap imagenBitmap; // Imagen que establece el usuario mediante la cámara o la galería
    private String nombre; // Nombre del usuario
    private String contraseña; // Contraseña del usuario
    private String correo; // Correo del usuario
    private ListaOfertas ofertas; // Lista de ofertas generadas por el usuario
    private ListaOfertas favoritas; // Lista de ofertas favoritas del usuario
    private HashMap<String, Chat> chats; // Lista de chats del usuario

    // Constructora que inicializa los atributos de la clase
    public Usuario(String pnombre, String pcontraseña, String pcorreo) {
        imagenDefault = R.drawable.user_icon;
        imagenBitmap = null;
        nombre = pnombre;
        contraseña = pcontraseña;
        correo = pcorreo;
        ofertas = new ListaOfertas();
        favoritas = new ListaOfertas();
        chats = new HashMap<String, Chat>();
    }

    // Método que devuelve la imagen por defecto
    public int getImagenDefault() {
        return imagenDefault;
    }

    // Método que devuelve la imagen que establece el usuario mediante la cámara o la galería
    public Bitmap getImagenBitmap() {
        return imagenBitmap;
    }

    // Método que devuelve el nombre del usuario
    public String getNombre() {
        return nombre;
    }

    // Método que devuelve la contraseña del usuario
    public String getContraseña() {
        return contraseña;
    }

    // Método que devuelve el correo del usuario
    public String getCorreo() {
        return correo;
    }

    // Método que devuelve la lista de ofertas generadas por el usuario
    public ListaOfertas getOfertas() {
        return ofertas;
    }

    // Método que devuelve la lista de ofertas favoritas del usuario
    public ListaOfertas getFavoritas() {
        return favoritas;
    }

    // Método que devuelve la lista de chats del usuario
    public HashMap<String, Chat> getChats() {
        return chats;
    }

    // Método que establece la imagen escogida por el usuario mediante la cámara o la galería
    public void setImagenBitmap(Bitmap imagenBitmap) {
        this.imagenBitmap = imagenBitmap;
    }
}
