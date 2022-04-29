package com.example.primeraentregaindividual;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

// Actividad que da la opción de registrarse al usuario
public class ActivityRegistrarse extends Activity implements ClaseDialogo.ListenerDialogo {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
    }

    // Método que registra al usuario tanto en la base de datos remota como en el catálogo de usuarios
    public void registrarse(View v) {
        EditText nombreUsuario = findViewById(R.id.nombreUsuario7);
        EditText contraseña = findViewById(R.id.contraseña5);
        EditText correo = findViewById(R.id.correo2);

        String nombreUsuario2 = nombreUsuario.getText().toString().trim();
        String contraseña2 = contraseña.getText().toString().trim();
        String correo2 = correo.getText().toString().trim();

        OneTimeWorkRequest trabajoPuntual = BBDD.getBBDD(this).selectCatalogoUsuarios();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(trabajoPuntual.getId())
                .observe(this, status -> {
                    if (status != null && status.getState().isFinished()) {
                        String result = status.getOutputData().getString("result");
                        BBDD.getBBDD(this).cargarCatalogoUsuarios(result);
                        HashMap<String, Usuario> lista = CatalogoUsuarios.getCatalogoUsuarios().getLista();
                        if(!nombreUsuario2.equals("") && !contraseña2.equals("") && !correo2.equals("")) {
                            if (!lista.containsKey(nombreUsuario2)) {
                                OneTimeWorkRequest trabajoPuntual2 = BBDD.getBBDD(this).insertUsuario(nombreUsuario2, contraseña2, correo2);
                                WorkManager.getInstance(this).getWorkInfoByIdLiveData(trabajoPuntual2.getId())
                                        .observe(this, status2 -> {
                                            if (status2 != null && status2.getState().isFinished()) {
                                                String result2 = status2.getOutputData().getString("result");

                                                Usuario usuario = new Usuario(nombreUsuario2, result2, correo2);
                                                lista.put(nombreUsuario2, usuario);
                                                CatalogoUsuarios.getCatalogoUsuarios().setUsuarioActual(usuario);

                                                DialogFragment dialogo = new ClaseDialogo(1,getString(R.string.registrarUsuarioTitulo),getString(R.string.registrarUsuarioMensaje),getString(R.string.ok),null,null);
                                                dialogo.setCancelable(false);
                                                dialogo.show(getSupportFragmentManager(), "etiqueta");
                                            }
                                        });

                                WorkManager.getInstance(this).enqueue(trabajoPuntual2);
                            }
                            else {
                                DialogFragment dialogo = new ClaseDialogo(0,getString(R.string.errorAlRegistrarse),getString(R.string.usuarioExistente),getString(R.string.ok),null,null);
                                dialogo.show(getSupportFragmentManager(), "etiqueta");
                            }
                        }
                        else {
                            DialogFragment dialogo = new ClaseDialogo(0,getString(R.string.errorAlRegistrarse),getString(R.string.camposVacios),getString(R.string.ok),null,null);
                            dialogo.show(getSupportFragmentManager(), "etiqueta");
                        }
                    }
                });

        WorkManager.getInstance(this).enqueue(trabajoPuntual);
    }

    // Al pulsar OK en el diálogo que informa de que el usuario se ha regisatrado correctamente
    // se escribe el login en el fichero de logs, se finaliza la actividad y se lanza la actividad de la página principal de la aplicación
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
    }

    // Al pulsar la tecla back del dispositivo se finaliza la actividad y
    // se lanza la actividad de inicio de sesión
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(this, ActivityIdentificarse.class);
        startActivity(i);
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
