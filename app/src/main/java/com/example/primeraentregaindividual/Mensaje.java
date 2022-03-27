package com.example.primeraentregaindividual;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Clase que representa un mensaje cualquiera
public class Mensaje {
    private String emisor; // Nombre del emisor del mensaje
    private String receptor; // Nombre del receptor del mensaje
    private String fechaHora; // Fecha y hora a la que el mensaje fue enviado
    private String texto; // Texto del mensaje

    // Constructora que inicializa los atributos de la clase
    public Mensaje(String pemisor, String preceptor, String ptexto) {
        emisor = pemisor;
        receptor = preceptor;
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss\ndd/MM/yyyy");
        String strDate = dateFormat.format(date);
        fechaHora = strDate;
        texto = ptexto;
    }

    // Método que devuelve el nombre del emisor
    public String getEmisor() {
        return emisor;
    }

    // Método que devuelve el nombre del receptor
    public String getReceptor() {
        return receptor;
    }

    // Método que devuelve la fecha y la hora a la que fue enviado el mensaje
    public String getFechaHora() {
        return fechaHora;
    }

    // Método que devuelve el texto del mensaje
    public String getTexto() {
        return texto;
    }
}
