package com.example.primeraentregaindividual;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

// Actividad principal de la aplicación que ofrece múltiples opciones
public class ActivityMain extends ActivityNoActionBar implements ClaseDialogo.ListenerDialogo {

    // Al crear la actividad se establece la toolbar
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.labarra));
    }

    // Método que lanza la actividad que muestra el catálogo de ofertas
    public void buscar(View v) {
        Intent i = new Intent (this, ActivityBuscar.class);
        i.putExtra("favoritos", false);
        startActivity(i);
    }

    // Método que lanza la actividad que da la opción de ofrecer un empleo o un servicio
    public void ofrecer(View v) {
        Intent i = new Intent (this, ActivityOfrecer.class);
        startActivity(i);
    }

    // Método que lanza la actividad que muestra la lista de ofertas favoritas del usuario
    public void mostrarFavoritos(View v) {
        Intent i = new Intent (this, ActivityBuscar.class);
        i.putExtra("favoritos", true);
        startActivity(i);
    }

    // Método que lanza la actividad que muestra los chats del usuario
    public void mostrarChats(View v) {
        Intent i = new Intent (this, ActivityMostrarChats.class);
        startActivity(i);
    }

    // Método que lanza la actividad que muestra el perfil del usuario
    public void mostrarPerfil(View v) {
        Usuario usuario = CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual();
        String nombre = usuario.getNombre();

        Intent i = new Intent (this, ActivityMostrarPerfil.class);
        i.putExtra("nombre", nombre);
        startActivity(i);
    }

    // Al pulsar SI en el diálogo de confirmación que se muestra al intentar salir de la aplicación
    // se cierra la aplicación
    // Al pulsar CERRAR SESIÓN en el diálogo de confirmación que se muestra al intentar salir de la aplicación
    // se cierra sesión y se vuelve a la actividad de inicio de sesión
    @Override
    public void alPulsarBoton(int codigo, String boton) {
        if(codigo==2) {
            if(boton.equals("positive")) {
                super.onBackPressed();
            }
            else if(boton.equals("neutral")) {
                CatalogoUsuarios.getCatalogoUsuarios().setUsuarioActual(null);

                finish();
                Intent i = new Intent(this, ActivityIdentificarse.class);
                startActivity(i);
            }
        }
    }

    // Al pulsar la tecla back del dispositivo se lanza un diálogo de confirmación
    @Override
    public void onBackPressed() {
        DialogFragment dialogo = new ClaseDialogo(2,getString(R.string.cerrarAplicacionTitulo),getString(R.string.cerrarAplicacionMensaje),getString(R.string.si),getString(R.string.no),getString(R.string.cerrar_sesi_n));
        dialogo.setCancelable(false);
        dialogo.show(getSupportFragmentManager(), "etiqueta");
    }

    // Método que lanza la actividad que da la opción de cambiar las preferencias del usuario
    public void ajustes(View v) {
        finish();
        Intent i = new Intent (this, ActivityConfigurar.class);
        startActivity(i);
    }
}
