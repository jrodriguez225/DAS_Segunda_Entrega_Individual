package com.example.primeraentregaindividual;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import java.util.ArrayList;
import java.util.HashMap;

// Actividad que muestra los chats del usuario
public class ActivityMostrarChats extends ActivityNoActionBar implements ClaseDialogo.ListenerDialogo, FragmentMostrarChats.ListenerFragment, FragmentChatear.ListenerFragment {
    private ArrayList<Chat> lista; // Lista de chats del usuario que se le pasan al adaptador del ListView
    private AdaptadorMostrarChats eladap; // Adaptador del ListView
    private int chatEliminado; // En caso de que se elimine algún chat se almacena su posición en la lista
    private ListView chats; // ListView que muestra los chats en el layout

    // Al crear la actividad se establece la toolbar
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_chats);

        setSupportActionBar(findViewById(R.id.labarra));
    }

    // Al pulsar SI en el diálogo de confirmación que se muestra al intentar eliminar un chat
    // se elimina el chat de la lista de chats del usuario y se muestra un toast que informa de ello
    @Override
    public void alPulsarBoton(int codigo, String boton) {
        if(codigo==1) {
            if(boton.equals("positive")) {
                Chat chat = lista.get(chatEliminado);
                Usuario emisor = chat.getEmisor();
                Usuario receptor = chat.getReceptor();

                mostrarToast(receptor.getNombre());

                emisor.getChats().remove(emisor.getNombre() + "+" + receptor.getNombre());
                lista.remove(chat);
                eladap.notifyDataSetChanged();
            }
        }
    }

    // Método que muestra un toast informando de que se ha eliminado un chat
    private void mostrarToast(String texto) {
        LayoutInflater inflater = getLayoutInflater();
        View el_layout = inflater.inflate(R.layout.toastlayout,(ViewGroup) findViewById(R.id.idlayout));
        TextView texto2 = el_layout.findViewById(R.id.texto2);
        texto2.setText(getText(R.string.toastChats) + " \"" + texto + "\"");
        Toast toastcustomizado = new Toast(this);
        toastcustomizado.setGravity(Gravity.TOP, 0, 225);
        toastcustomizado.setDuration(Toast.LENGTH_LONG);
        toastcustomizado.setView(el_layout);
        toastcustomizado.show();
    }

    // Método que muestra los chats
    @Override
    public void mostrarChats(View view) {
        lista = new ArrayList<Chat>(CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual().getChats().values());

        chats = (ListView) view.findViewById(R.id.listaChats);

        eladap = new AdaptadorMostrarChats(getApplicationContext(), lista);
        chats.setAdapter(eladap);

        chats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Chat chat = lista.get(position);
                Usuario usuario = chat.getReceptor();
                String nombre = usuario.getNombre();

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //EL OTRO FRAGMENT EXISTE
                    FragmentChatear fragmentChatear = (FragmentChatear) getSupportFragmentManager().findFragmentById(R.id.fragmentChatear);
                    fragmentChatear.mostrarChat(nombre);
                }
                else {
                    //EL OTRO FRAGMENT NO EXISTE, HAY QUE LANZAR LA ACTIVIDAD QUE LO CONTIENE
                    Intent i = new Intent(view.getContext(), ActivityChatear.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("nombre", nombre);
                    startActivity(i);
                }
            }
        });
        chats.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                chatEliminado = i;

                DialogFragment dialogo = new ClaseDialogo(1,getString(R.string.eliminarChatTitulo),getString(R.string.eliminarChatMensaje),getString(R.string.si),getString(R.string.no),null);
                dialogo.setCancelable(false);
                dialogo.show(getSupportFragmentManager(), "etiqueta");

                return true;
            }
        });
    }

    // Método que muestra un chat
    @Override
    public void mostrarChat(View view, String nombreReceptor) {
        Usuario emisor = CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual();
        Usuario receptor = CatalogoUsuarios.getCatalogoUsuarios().getLista().get(nombreReceptor);

        ImageView imagen = view.findViewById(R.id.imagen3);
        TextView nombre = view.findViewById(R.id.nombre3);
        ListView mensajes = (ListView) view.findViewById(R.id.chat);

        HashMap<String, Chat> listaEmisor = emisor.getChats();
        String key1 = emisor.getNombre() + "+" + receptor.getNombre();
        Chat chatEmisor = null;
        if(listaEmisor.containsKey(key1)) {
            chatEmisor = listaEmisor.get(key1);
        }
        else {
            chatEmisor = new Chat(emisor, receptor);
            listaEmisor.put(key1, chatEmisor);
        }
        ArrayList<Mensaje> listaMensajesEmisor = chatEmisor.getLista();
        AdaptadorChatear eladap = new AdaptadorChatear(getApplicationContext(), listaMensajesEmisor);

        Bitmap imageBitmap = receptor.getImagenBitmap();
        if (imageBitmap!=null) {
            imagen.setImageBitmap(imageBitmap);
        }
        else {
            imagen.setImageResource(receptor.getImagenDefault());
        }
        nombre.setText(receptor.getNombre());
        mensajes.setAdapter(eladap);
    }
}