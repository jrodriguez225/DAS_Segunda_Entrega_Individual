package com.example.primeraentregaindividual;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

// Adaptador del ListView que muestra los mensajes del chat entre el usuario actual y otro usuario
public class AdaptadorChatear extends BaseAdapter {

    private Context contexto; // Contexto de la actividad
    private LayoutInflater inflater; // Inflater que establece el layout que se considera una fila de la ListView
    private ArrayList<Mensaje> lista; // Lista de mensajes

    // Constructora del adaptador en la que se inicializan los atributos
    public AdaptadorChatear(Context pcontext, ArrayList<Mensaje> plista)
    {
        contexto = pcontext;
        lista = plista;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // Método que define que debe mostrar una fila de la lista
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fila_chatear,null);
        TextView emisor = (TextView) view.findViewById(R.id.emisor);
        TextView receptor = (TextView) view.findViewById(R.id.receptor);
        TextView fechaHora= (TextView) view.findViewById(R.id.fechaHora);
        TextView texto = (TextView) view.findViewById(R.id.texto);

        Mensaje mensaje = lista.get(i);
        String emisor2 = mensaje.getEmisor();
        String receptor2 = mensaje.getReceptor();
        String fechaHora2 = mensaje.getFechaHora();
        String texto2 = mensaje.getTexto();

        emisor.setText(emisor2);
        receptor.setText(receptor2);
        fechaHora.setText(fechaHora2);
        texto.setText(texto2);

        return view;
    }
}
