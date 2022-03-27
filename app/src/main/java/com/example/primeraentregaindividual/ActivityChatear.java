package com.example.primeraentregaindividual;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import java.util.ArrayList;
import java.util.HashMap;

// Actividad en la que se puede entablar una conversación con otro usuario
public class ActivityChatear extends ActivityNoActionBar implements ClaseDialogo.ListenerDialogo, FragmentChatear.ListenerFragment {
    private Usuario emisor; // Emisor de la conversación, el usuario actual
    private Usuario receptor; // Receptor de la conversación, el otro usuario
    private ArrayList<Mensaje> listaMensajesEmisor; // Lista de mensajes del emisor que se le pasa al adaptador del ListView
    private ArrayList<Mensaje> listaMensajesReceptor; // Lista de mensajes del receptor
    private AdaptadorChatear eladap; // Adaptador del ListView
    private int mensajeEliminado; // En caso de que se elimine algún mensaje se almacena su posición en la lista

    // Al crear la actividad se establece la toolbar y se muestra el chat
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatear);

        setSupportActionBar(findViewById(R.id.labarra));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nombreReceptor = extras.getString("nombre");

            FragmentChatear fragmentChatear = (FragmentChatear) getSupportFragmentManager().findFragmentById(R.id.fragmentChatear);
            fragmentChatear.mostrarChat(nombreReceptor);
        }
    }

    // Método que almacena el mensaje enviado tanto en la lista del emisor como del recptor
    public void enviarMensaje(View v) {
        EditText mensaje = findViewById(R.id.mensaje);
        String texto = mensaje.getText().toString().trim();
        mensaje.setText("");

        Mensaje mensaje2 = new Mensaje(emisor.getNombre(), receptor.getNombre(), texto);
        listaMensajesEmisor.add(mensaje2);
        listaMensajesReceptor.add(mensaje2);

        eladap.notifyDataSetChanged();
    }

    // Método que lanza la actividad en la que se muestra el perfil del receptor
    private void mostrarPerfil() {
        Usuario usuario = receptor;
        String nombre = usuario.getNombre();

        Intent i = new Intent (this, ActivityMostrarPerfil.class);
        i.putExtra("nombre", nombre);
        startActivity(i);
    }

    // Al pulsar SI en el diálogo de confirmación que se muestra al intentar eliminar un mensaje
    // se elimina el mensaje de la lista de mensajes del emisor y se muestra un toast que informa de ello
    @Override
    public void alPulsarBoton(int codigo, String boton) {
        if(codigo==1) {
            if(boton.equals("positive")) {
                Mensaje mensaje = listaMensajesEmisor.get(mensajeEliminado);

                String texto = mensaje.getTexto();
                mostrarToast(texto);

                listaMensajesEmisor.remove(mensaje);
                eladap.notifyDataSetChanged();
            }
        }
    }

    // Método que muestra un toast informando de que se ha eliminado un mensaje
    private void mostrarToast(String texto) {
        LayoutInflater inflater = getLayoutInflater();
        View el_layout = inflater.inflate(R.layout.toastlayout,(ViewGroup) findViewById(R.id.idlayout));
        TextView texto2 = el_layout.findViewById(R.id.texto2);
        texto2.setText(getText(R.string.toastMensajes) + " \"" + texto + "\"");
        Toast toastcustomizado = new Toast(this);
        toastcustomizado.setGravity(Gravity.TOP, 0, 225);
        toastcustomizado.setDuration(Toast.LENGTH_LONG);
        toastcustomizado.setView(el_layout);
        toastcustomizado.show();
    }

    // Método que muestra el chat
    @Override
    public void mostrarChat(View view, String nombreReceptor) {
        emisor = CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual();
        receptor = CatalogoUsuarios.getCatalogoUsuarios().getLista().get(nombreReceptor);

        ImageView imagen = view.findViewById(R.id.imagen3);
        TextView nombre = view.findViewById(R.id.nombre3);
        ListView mensajes = (ListView) view.findViewById(R.id.chat);

        HashMap<String, Chat> listaEmisor = emisor.getChats();
        HashMap<String, Chat> listaReceptor = receptor.getChats();
        String key1 = emisor.getNombre() + "+" + receptor.getNombre();
        String key2 = receptor.getNombre() + "+" + emisor.getNombre();
        Chat chatEmisor = null;
        Chat chatReceptor = null;
        if(listaEmisor.containsKey(key1)) {
            chatEmisor = listaEmisor.get(key1);
        }
        else {
            chatEmisor = new Chat(emisor, receptor);
            listaEmisor.put(key1, chatEmisor);
        }
        if(listaReceptor.containsKey(key2)) {
            chatReceptor = listaReceptor.get(key2);
        }
        else {
            chatReceptor = new Chat(receptor, emisor);
            listaReceptor.put(key2, chatReceptor);
        }
        listaMensajesEmisor = chatEmisor.getLista();
        listaMensajesReceptor = chatReceptor.getLista();
        eladap = new AdaptadorChatear(getApplicationContext(), listaMensajesEmisor);

        Bitmap imageBitmap = receptor.getImagenBitmap();
        if (imageBitmap!=null) {
            imagen.setImageBitmap(imageBitmap);
        }
        else {
            imagen.setImageResource(receptor.getImagenDefault());
        }
        nombre.setText(receptor.getNombre());
        mensajes.setAdapter(eladap);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarPerfil();
            }
        });
        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarPerfil();
            }
        });
        mensajes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mensajeEliminado = i;

                DialogFragment dialogo = new ClaseDialogo(1,getString(R.string.eliminarMensajeTitulo),getString(R.string.eliminarMensajeMensaje),getString(R.string.si),getString(R.string.no),null);
                dialogo.setCancelable(false);
                dialogo.show(getSupportFragmentManager(), "etiqueta");

                return true;
            }
        });
    }
}
