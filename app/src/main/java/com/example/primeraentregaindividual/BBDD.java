package com.example.primeraentregaindividual;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

// Base de datos remota de la aplicación
public class BBDD {

    private static BBDD miBBDD; // La clase es una MAE (instancia única) por lo que se hace referencia a la clase mediante un atributo estático
    private static Context context; // Contexto de la actividad

    // Constructora de la base de datos
    private BBDD() {}

    // Método estático que devuelve la instancia de la clase
    public static BBDD getBBDD(Context pcontext) {
        if (miBBDD==null) {
            miBBDD = new BBDD();
        }
        context = pcontext;
        return miBBDD;
    }

    // Método que ejecuta un servicio web que se encarga de hacer una lectura de todos los usuarios almacenados en la base de datos remota
    public OneTimeWorkRequest selectCatalogoUsuarios() {
        Data datos = new Data.Builder()
                .putString("servicioweb", "selectCatalogoUsuarios")
                .build();

        return ejecutarServicioWeb(datos);
    }

    // Método que ejecuta un servicio web que se encarga de almacenar un usuario en la base de datos remota
    public OneTimeWorkRequest insertUsuario(String nombre, String contraseña, String correo) {
        Data datos = new Data.Builder()
                .putString("servicioweb", "insertUsuario")
                .putString("nombre",nombre)
                .putString("contraseña", contraseña)
                .putString("correo", correo)
                .build();

        return ejecutarServicioWeb(datos);
    }

    // Método que ejecuta un servicio web que se encarga de verificar si coinciden la contraseña introducida y la del usuario
    public OneTimeWorkRequest verificarContraseña(String contraseña, String hash) {
        Data datos = new Data.Builder()
                .putString("servicioweb", "passwordVerify")
                .putString("contraseña",contraseña)
                .putString("hash", hash)
                .build();

        return ejecutarServicioWeb(datos);
    }

    // Método que ejecuta un servicio web que se encarga de almacenar el token del dispositivo en la base de datos remota
    public OneTimeWorkRequest insertToken(String token) {
        Data datos = new Data.Builder()
                .putString("servicioweb", "insertToken")
                .putString("token", token)
                .build();

        return ejecutarServicioWeb(datos);
    }

    // Método que ejecuta un servicio web que se encarga de actualizar la imagen asociada a un usuario en la base de datos remota
    public OneTimeWorkRequest updateImagen(String imagen, String usuario) {
        Data datos = new Data.Builder()
                .putString("servicioweb", "updateImagen")
                .putString("imagen", imagen)
                .putString("usuario", usuario)
                .build();

        return ejecutarServicioWeb(datos);
    }

    // Método que ejecuta un servicio web que se encarga de enviar un mensaje a todos los dispositivos almacenados en la base de datos remota
    public OneTimeWorkRequest enviarMensaje() {
        Data datos = new Data.Builder()
                .putString("servicioweb", "enviarMensaje")
                .build();

        return ejecutarServicioWeb(datos);
    }

    // Método que ejecuta un servicio de manera asíncrona, para ello lo ejecuta como si fuese una tarea
    private OneTimeWorkRequest ejecutarServicioWeb(Data datos) {
        Constraints restricciones = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest trabajoPuntual =
                new OneTimeWorkRequest.Builder(ConexionServidorDAS.class)
                        .setConstraints(restricciones)
                        .setInputData(datos)
                        .setBackoffCriteria(BackoffPolicy.LINEAR,3, TimeUnit.SECONDS)
                        .build();

        return trabajoPuntual;
    }

    // Método que carga el catalogo de usuarios a partir del resultado obtenido tras hacer la lectura de todos los usuarios almacenados en la base de datos remota
    public void cargarCatalogoUsuarios(String result) {
        if (!result.equals("null")) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                HashMap<String, Usuario> lista = CatalogoUsuarios.getCatalogoUsuarios().getLista();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);

                    String nombre = json.getString("nombre");
                    if (!lista.containsKey(nombre)) {
                        String contraseña = json.getString("contrasena");
                        String correo = json.getString("correo");
                        String imagen = json.getString("imagen");

                        Usuario usuario = new Usuario(nombre, contraseña, correo);
                        if (!imagen.equals("null")) {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imagen));
                            usuario.setImagenBitmap(bitmap);
                        }
                        lista.put(nombre, usuario);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
