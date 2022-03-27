package com.example.primeraentregaindividual;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import java.util.HashMap;

// Actividad que da la opción al usuario de recuperar su contraseña
public class ActivityRecuperarContraseña extends Activity implements ClaseDialogo.ListenerDialogo {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);
    }

    // Método que recupera la contraseña del usuario, para ello le envía un correo con la contraseña al email asociado a su cuenta
    public void recuperarContraseña(View v) {
        EditText nombreUsuario = findViewById(R.id.nombreUsuario6);

        String nombreUsuario2 = nombreUsuario.getText().toString().trim();

        BBDD.getBBDD(this).selectCatalogoUsuarios();
        HashMap<String, Usuario> lista = CatalogoUsuarios.getCatalogoUsuarios().getLista();
        DialogFragment dialogo;
        if(!nombreUsuario2.equals("")) {
            if (lista.containsKey(nombreUsuario2)) {
                Usuario usuario = lista.get(nombreUsuario2);
                String address = usuario.getCorreo();
                String subject = getString(R.string.recuperarContraseñaTitulo);
                String body = getString(R.string.mail) + " " + usuario.getContraseña();

                String user = "jonro1921@gmail.com";
                String password = "dmnarfnbnvvyjdbv";

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            MailSender sender = new MailSender(user, password);
                            sender.sendMail(subject, body, user, address);
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }

                }).start();

                dialogo = new ClaseDialogo(1,getString(R.string.recuperarContraseñaTitulo),getString(R.string.recuperarContraseñaMensaje),getString(R.string.ok),null,null);
                dialogo.setCancelable(false);
            }
            else {
                dialogo = new ClaseDialogo(0,getString(R.string.errorAlRecuperarContraseña),getString(R.string.usuarioNoExistente),getString(R.string.ok),null,null);
            }
        }
        else {
            dialogo = new ClaseDialogo(0,getString(R.string.errorAlRecuperarContraseña),getString(R.string.camposVacios),getString(R.string.ok),null,null);
        }
        dialogo.show(getSupportFragmentManager(), "etiqueta");
    }

    // Al pulsar OK en el diálogo que informa de que se ha enviado la contraseña al correo asociado al usuario
    // se finaliza la actividad
    @Override
    public void alPulsarBoton(int codigo, String boton) {
        if(codigo==1) {
            if(boton.equals("positive")) {
                finish();
            }
        }
    }
}
