package com.example.primeraentregaindividual;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
// Clase que establece la conexión entre la aplicación y el servidor remoto
public class ConexionServidorDAS extends Worker {

    // Constructora de la clase
    public ConexionServidorDAS(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    // Método que ejecuta los distintos servicios web de los que dispone el servidor y devuelve el resultado
    @NonNull
    @Override
    public Result doWork() {
        String servicioweb = getInputData().getString("servicioweb");
        String result = null;
        if(servicioweb.equals("selectCatalogoUsuarios")) {
            result = llamarServicioWeb(servicioweb, null);
        }
        else if(servicioweb.equals("insertUsuario")) {
            String nombre = getInputData().getString("nombre");
            String contraseña = getInputData().getString("contraseña");
            String correo = getInputData().getString("correo");

            String parametros = "nombre="+nombre+"&contraseña="+contraseña+"&correo="+correo;

            result = llamarServicioWeb(servicioweb, parametros);
        }
        else if(servicioweb.equals("passwordVerify")) {
            String contraseña = getInputData().getString("contraseña");
            String hash = getInputData().getString("hash");

            String parametros = "contraseña="+contraseña+"&hash="+hash;

            result = llamarServicioWeb(servicioweb, parametros);
        }
        else if(servicioweb.equals("insertToken")) {
            String token = getInputData().getString("token");

            String parametros = "token="+token;

            result = llamarServicioWeb(servicioweb, parametros);
        }
        else if(servicioweb.equals("updateImagen")) {
            String imagen = getInputData().getString("imagen");
            String usuario = getInputData().getString("usuario");

            String parametros = "imagen="+imagen+"&usuario="+usuario;

            result = llamarServicioWeb(servicioweb, parametros);
        }
        else if(servicioweb.equals("enviarMensaje")) {
            result = llamarServicioWeb(servicioweb, null);
        }

        if (result!=null) {
            Data resultados = new Data.Builder()
                    .putString("result", result)
                    .build();
            return Result.success(resultados);
        }
        else {
            return Result.retry();
        }
    }

    // Método que entabla la conexión con el servidor, ejecuta el servicio web y devuelve el resultado
    private String llamarServicioWeb(String servicioweb, String parametros) {
        String result = null;

        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/jrodriguez225/WEB/" + servicioweb + ".php";

        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            if (parametros!=null) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(parametros);
                out.close();
            }

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
