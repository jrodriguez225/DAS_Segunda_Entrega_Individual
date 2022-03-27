package com.example.primeraentregaindividual;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import java.util.HashMap;

// Actividad que da la opción al usuario de ofrecer un empleo o servicio
public class ActivityOfrecer extends ActivityNoActionBar implements ClaseDialogo.ListenerDialogo {

    // Al crear la actividad se establece la toolbar
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofrecer);

        setSupportActionBar(findViewById(R.id.labarra));
    }

    // Método que crea un empleo
    public void crearEmpleo(View v) {
        crearOferta("empleo");
    }

    // Método que crea un servicio
    public void crearServicio(View v) {
        crearOferta("servicio");
    }

    // Método genérico que crea una oferta y la añade tanto al catálogo de ofertas como a la lista de ofertas del usuario
    private void crearOferta(String tipo) {
        EditText nombre = findViewById(R.id.nombreOferta);
        EditText descripcion = findViewById(R.id.descripcionOferta);

        Usuario usuario = CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual();
        String nombreOferta = nombre.getText().toString().trim();
        String descripcion2 = descripcion.getText().toString().trim();
        String nombreUsuario = usuario.getNombre();

        HashMap<String, Oferta> lista = CatalogoOfertas.getCatalogoOfertas().getLista();
        DialogFragment dialogo;
        if(!nombreOferta.equals("") && !descripcion2.equals("")) {
            Oferta oferta = null;
            if(!lista.containsKey(nombreUsuario + "+" + nombreOferta)) {
                if (tipo.equals("empleo")) {
                    oferta = new Empleo(nombreOferta, descripcion2, usuario);
                } else if (tipo.equals("servicio")) {
                    oferta = new Servicio(nombreOferta, descripcion2, usuario);
                }
                usuario.getOfertas().getLista().add(oferta);
                lista.put(nombreUsuario + "+" + nombreOferta, oferta);

                dialogo = new ClaseDialogo(1,getString(R.string.crearOfertaTitulo),getString(R.string.crearOfertaMensaje),getString(R.string.ok),null,null);
            }
            else {
                oferta = lista.get(nombreUsuario + "+" + nombreOferta);
                oferta.setDescripcion(descripcion2);

                dialogo = new ClaseDialogo(1,getString(R.string.modificarOfertaTitulo),getString(R.string.modificarOfertaMensaje),getString(R.string.ok),null,null);
            }
            dialogo.setCancelable(false);
        }
        else {
            dialogo = new ClaseDialogo(0,getString(R.string.errorAlCrearOferta),getString(R.string.camposVacios),getString(R.string.ok),null,null);
        }
        dialogo.show(getSupportFragmentManager(), "etiqueta");
    }

    // Al pulsar OK en el diálogo que informa de que se ha creado o modificado la oferta correctamente
    // se finaliza la actividad
    @Override
    public void alPulsarBoton(int codigo, String boton) {
        if(codigo==1) {
            if(boton.equals("positive")){
                finish();
            }
        }
    }
}
