package com.example.primeraentregaindividual;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;

// Actividad que muestra la oferta seleccionada
public class ActivityMostrarOferta extends ActivityNoActionBar implements FragmentMostrarOferta.ListenerFragment {
    private String nombreUsuario;

    // Al crear la actividad se establece la toolbar y se muestra la oferta
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_oferta);

        setSupportActionBar(findViewById(R.id.labarra));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nombreUsuario = extras.getString("nombreUsuario");
            String nombreOferta = extras.getString("nombreOferta");
            String descripcion = extras.getString("descripcion");

            FragmentMostrarOferta fragmentMostrarOferta = (FragmentMostrarOferta) getSupportFragmentManager().findFragmentById(R.id.fragmentMostrarOferta);
            fragmentMostrarOferta.mostrarOferta(nombreUsuario, nombreOferta, descripcion);
        }
    }

    // Método que lanza la actividad en la que se muestra el perfil
    private void mostrarPerfil() {
        Intent i = new Intent (this, ActivityMostrarPerfil.class);
        i.putExtra("nombre", nombreUsuario);
        startActivity(i);
    }

    // Método que lanza la actividad en la que se entabla conversación con otro usuario
    public void contactar(View v) {
        Intent i = new Intent(this, ActivityChatear.class);
        i.putExtra("nombre", nombreUsuario);
        startActivity(i);
    }

    // Método que muestra la oferta
    @Override
    public void mostrarOferta(View view, String nombreUsuario2, String nombreOferta, String descripcion) {
        nombreUsuario = nombreUsuario2;

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
        if(nombreUsuario.equals(CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual().getNombre())) {
            ImageView contactar = findViewById(R.id.contactar2);
            contactar.setVisibility(View.INVISIBLE);
        }
        else {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mostrarPerfil();
                }
            });
            nomUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mostrarPerfil();
                }
            });
        }
    }
}
