package com.example.primeraentregaindividual;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

// Actividad en la que el usuario inicia sesión
public class ActivityIdentificarse extends Activity implements ClaseDialogo.ListenerDialogo {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificarse);
    }

    // Método que finaliza la actividad y lanza la actividad de registro de usuarios
    public void registrarse(View v) {
        finish();
        Intent i = new Intent(this, ActivityRegistrarse.class);
        startActivity(i);
    }

    // Método que lanza la actividad de recuperación de contraseña
    public void recuperarContraseña(View v) {
        Intent i = new Intent(this, ActivityRecuperarContraseña.class);
        startActivity(i);
    }

    // Método que permite iniciar sesión al usuario
    public void iniciarSesion(View v) {
        EditText nombreUsuario = findViewById(R.id.nombreUsuario4);
        EditText contraseña = findViewById(R.id.contraseña2);

        String nombreUsuario2 = nombreUsuario.getText().toString().trim();
        String contraseña2 = contraseña.getText().toString().trim();

        BBDD.getBBDD(this).selectCatalogoUsuarios();
        HashMap<String, Usuario> lista = CatalogoUsuarios.getCatalogoUsuarios().getLista();
        DialogFragment dialogo;
        if(!nombreUsuario2.equals("") && !contraseña2.equals("")) {
            if (lista.containsKey(nombreUsuario2)) {
                Usuario usuario = lista.get(nombreUsuario2);
                if (contraseña2.equals(usuario.getContraseña())) {
                    CatalogoUsuarios.getCatalogoUsuarios().setUsuarioActual(usuario);

                    dialogo = new ClaseDialogo(1,getString(R.string.identificacionUsuarioTitulo),getString(R.string.identificacionUsuarioMensaje),getString(R.string.ok),null,null);
                    dialogo.setCancelable(false);
                }
                else {
                    dialogo = new ClaseDialogo(0,getString(R.string.errorAlIniciarSesion),getString(R.string.credencialesErroneas),getString(R.string.ok),null,null);
                }
            }
            else {
                dialogo = new ClaseDialogo(0,getString(R.string.errorAlIniciarSesion),getString(R.string.usuarioNoExistente),getString(R.string.ok),null,null);
            }
        }
        else {
            dialogo = new ClaseDialogo(0,getString(R.string.errorAlIniciarSesion),getString(R.string.camposVacios),getString(R.string.ok),null,null);
        }
        dialogo.show(getSupportFragmentManager(), "etiqueta");
    }

    // Al pulsar OK en el diálogo que informa de que se ha iniciado sesión correctamente
    // se escribe el login en el fichero de logs, se finaliza la actividad y se lanza la actividad de la página principal de la aplicación
    // Al pulsar SI en el diálogo de confirmación que se muestra al intentar salir de la aplicación
    // se cierra la aplicación
    @Override
    public void alPulsarBoton(int codigo, String boton) {
        if(codigo==1) {
            if(boton.equals("positive")) {
                escribirLogin();
                finish();
                Intent i = new Intent(this, ActivityMain.class);
                startActivity(i);
            }
        }
        else if(codigo==2) {
            if(boton.equals("positive")) {
                super.onBackPressed();
            }
        }
    }

    // Al pulsar la tecla back del dispositivo se lanza un diálogo de confirmación
    @Override
    public void onBackPressed() {
        DialogFragment dialogo = new ClaseDialogo(2,getString(R.string.cerrarAplicacionTitulo),getString(R.string.cerrarAplicacionMensaje),getString(R.string.si),getString(R.string.no),null);
        dialogo.setCancelable(false);
        dialogo.show(getSupportFragmentManager(), "etiqueta");
    }

    // Método que escribe el login del usuario en fichero de logs de los usuarios
    private void escribirLogin() {
        String estado = Environment.getExternalStorageState();
        if (estado.equals(Environment.MEDIA_MOUNTED))
        {
            File path = this.getExternalFilesDir(null);
            File f = new File(path.getAbsolutePath(), "user_logs.txt");
            boolean append = false;
            if (f.exists()) {
                append = true;
            }
            Log.i("FICH","PATH: " + path.getAbsolutePath());
            try {
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                String strDate = dateFormat.format(date);

                OutputStreamWriter ficheroexterno = new OutputStreamWriter(new FileOutputStream(f, append));
                ficheroexterno.append(strDate + " " + CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual().getNombre() + "\n");
                ficheroexterno.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}