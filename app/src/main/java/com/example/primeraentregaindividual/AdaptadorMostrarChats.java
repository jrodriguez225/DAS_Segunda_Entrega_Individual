package com.example.primeraentregaindividual;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

// Adaptador del ListView que muestra los chats del usuario
public class AdaptadorMostrarChats extends BaseAdapter {

    private Context contexto; // Contexto de la actividad
    private LayoutInflater inflater; // Inflater que establece el layout que se considera una fila de la ListView
    private ArrayList<Chat> lista; // Lista de chats del usuario

    // Constructora del adaptador en la que se inicializan los atributos
    public AdaptadorMostrarChats(Context pcontext, ArrayList<Chat> plista)
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

    // Método que define que debe mostrar una fila de la lista y las acciones correspondientes a cada elemento
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fila_mostrar_chats,null);
        ImageView img = (ImageView) view.findViewById(R.id.imagen5);
        TextView nom = (TextView) view.findViewById(R.id.nombre4);

        Chat chat = lista.get(i);
        Usuario usuario = chat.getReceptor();

        String nombre = usuario.getNombre();

        Bitmap imageBitmap = usuario.getImagenBitmap();
        if (imageBitmap!=null) {
            img.setImageBitmap(imageBitmap);
        }
        else {
            img.setImageResource(usuario.getImagenDefault());
        }
        nom.setText(nombre);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPerfil(nombre);
            }
        });
        nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPerfil(nombre);
            }
        });

        return view;
    }

    // Método que lanza la actividad que muestra el perfil de un usuario
    private void mostrarPerfil(String nombreUsuario) {
        Intent i = new Intent (contexto, ActivityMostrarPerfil.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("nombre", nombreUsuario);
        contexto.startActivity(i);
    }
}

