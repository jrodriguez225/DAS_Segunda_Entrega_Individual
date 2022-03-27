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

// Actividad en la que se hace la búsqueda tanto de empleos, como de servicios como de ambos
public class ActivityBuscar extends ActivityNoActionBar implements ClaseDialogo.ListenerDialogo, FragmentBuscar.ListenerFragment, FragmentMostrarOferta.ListenerFragment {
    private boolean favoritos; // Si es true la búsqueda se hace en base a los favoritos del usuario, si no en base al catálogo de ofertas
    private ArrayList<Oferta> lista; // La lista de ofertas que se le pasa al adaptador del ListView
    private AdaptadorBuscar eladap; // El adaptador del ListView
    private int ofertaEliminada; // En caso de que se elimine algún favorito se almacena su posición en la lista

    // Al crear la actividad se establece la toolbar
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        setSupportActionBar(findViewById(R.id.labarra));
    }

    // Método que muestra un toast informando de que se ha eliminado un favorito
    private void mostrarToast(String texto) {
        LayoutInflater inflater = getLayoutInflater();
        View el_layout = inflater.inflate(R.layout.toastlayout,(ViewGroup) findViewById(R.id.idlayout));
        TextView texto2 = el_layout.findViewById(R.id.texto2);
        texto2.setText(getText(R.string.toastFavoritos) + " \"" + texto + "\"");
        Toast toastcustomizado = new Toast(this);
        toastcustomizado.setGravity(Gravity.TOP, 0, 225);
        toastcustomizado.setDuration(Toast.LENGTH_LONG);
        toastcustomizado.setView(el_layout);
        toastcustomizado.show();
    }

    // Método que establece el filtro de búsqueda en empleos
    public void mostrarEmpleos(View v) {
        mostrar("empleos");
    }

    // Método que establece el filtro de búsqueda en servicios
    public void mostrarServicios(View v) {
        mostrar("servicios");
    }

    // Método que suprime el filtro de búsqueda
    public void mostrarAmbos(View v) {
        mostrar(null);
    }

    // Método que aplica el filtro de búsqueda seleccionado
    private void mostrar(String filtro) {
        finish();
        Intent i = new Intent(this, ActivityBuscar.class);
        i.putExtra("favoritos", favoritos);
        i.putExtra("filtro", filtro);
        startActivity(i);
    }

    // Al pulsar SI en el diálogo de confirmación que se muestra al intentar eliminar un favorito
    // se elimina la oferta de la lista de favoritos del usuario y se muestra un toast que informa de ello
    @Override
    public void alPulsarBoton(int codigo, String boton) {
        if(codigo==1) {
            if(boton.equals("positive")) {
                Oferta oferta = lista.get(ofertaEliminada);

                String texto = oferta.getNombre();
                mostrarToast(texto);

                //BBDD.getBBDD(this).deleteFavorita(oferta);
                CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual().getFavoritas().getLista().remove(oferta);
                lista.remove(oferta);
                eladap.notifyDataSetChanged();
            }
        }
    }

    // Método que muestra las ofertas
    @Override
    public void mostrarOfertas(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            favoritos = extras.getBoolean("favoritos");
            String filtro = extras.getString("filtro");

            TextView filtro2 = findViewById(R.id.filtro);
            ListView ofertas = (ListView) view.findViewById(R.id.listaOfertas);

            ListaOfertas plista = null;
            if(favoritos) {
                plista = CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual().getFavoritas();
            }
            else {
                plista = new ListaOfertas(CatalogoOfertas.getCatalogoOfertas().getLista().values());
            }
            lista = plista.filtrarListaOfertas(filtro);

            eladap = new AdaptadorBuscar(getApplicationContext(), favoritos, lista);

            if(filtro==null) {
                filtro2.setText(R.string.ofertas);
            }
            else {
                if (filtro.equals("empleos")) {
                    filtro2.setText(R.string.empleos);
                }
                else if (filtro.equals("servicios")) {
                    filtro2.setText(R.string.servicios);
                }
            }
            ofertas.setAdapter(eladap);
            ofertas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    Oferta oferta = lista.get(position);
                    Usuario usuario = oferta.getUsuario();

                    String nombreUsuario = usuario.getNombre();
                    String nombreOferta = oferta.getNombre();
                    String descripcion = oferta.getDescripcion();

                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        //EL OTRO FRAGMENT EXISTE
                        FragmentMostrarOferta fragmentMostrarOferta = (FragmentMostrarOferta) getSupportFragmentManager().findFragmentById(R.id.fragmentMostrarOferta);
                        fragmentMostrarOferta.mostrarOferta(nombreUsuario, nombreOferta, descripcion);
                    }
                    else {
                        //EL OTRO FRAGMENT NO EXISTE, HAY QUE LANZAR LA ACTIVIDAD QUE LO CONTIENE
                        Intent i = new Intent(view.getContext(), ActivityMostrarOferta.class);
                        i.putExtra("nombreUsuario", nombreUsuario);
                        i.putExtra("nombreOferta", nombreOferta);
                        i.putExtra("descripcion", descripcion);
                        startActivity(i);
                    }
                }
            });
            if(favoritos) {
                ofertas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ofertaEliminada = i;

                        DialogFragment dialogo = new ClaseDialogo(1,getString(R.string.eliminarFavoritoTitulo),getString(R.string.eliminarFavoritoMensaje),getString(R.string.si),getString(R.string.no),null);
                        dialogo.setCancelable(false);
                        dialogo.show(getSupportFragmentManager(), "etiqueta");

                        return true;
                    }
                });
            }
        }
    }


    // Método que muestra una oferta
    @Override
    public void mostrarOferta(View view, String nombreUsuario, String nombreOferta, String descripcion) {
        ImageView img = view.findViewById(R.id.imagen4);
        TextView nomUsuario = view.findViewById(R.id.nombreUsuario);
        TextView nomOferta = view.findViewById(R.id.nombreOferta2);
        TextView des = view.findViewById(R.id.descripcion);

        Usuario usuario = CatalogoUsuarios.getCatalogoUsuarios().getLista().get(nombreUsuario);
        Bitmap imageBitmap = usuario.getImagenBitmap();
        if (imageBitmap!=null) {
            img.setImageBitmap(imageBitmap);
        }
        else {
            img.setImageResource(usuario.getImagenDefault());
        }
        nomUsuario.setText(nombreUsuario);
        nomOferta.setText(nombreOferta);
        des.setText(descripcion);
    }
}
