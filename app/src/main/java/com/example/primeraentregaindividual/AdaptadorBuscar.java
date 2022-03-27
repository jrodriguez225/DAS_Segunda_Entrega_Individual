package com.example.primeraentregaindividual;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import java.util.ArrayList;

// Adaptador del ListView que muestra las ofertas, los empleos y los servicios
public class AdaptadorBuscar extends BaseAdapter {

    private Context contexto; // Contexto de la actividad
    private LayoutInflater inflater; // Inflater que establece el layout que se considera una fila de la ListView
    private boolean favoritos; // Si es true la búsqueda se hace en base a los favoritos del usuario, si no en base al catálogo de ofertas
    private ArrayList<Oferta> lista; // La lista de ofertas
    private NotificationManager elManager; // Manager de las notificaciones
    private NotificationCompat.Builder elBuilder; // Builder de las notificaciones

    // Constructora del adaptador en la que se inicializan los atributos y
    // se define la notificación que tiene que saltar al añadir una oferta a favoritos
    public AdaptadorBuscar(Context pcontext, boolean pfavoritos, ArrayList<Oferta> plista)
    {
        contexto = pcontext;
        favoritos = pfavoritos;
        lista = plista;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (!favoritos) {
            elManager = (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);
            elBuilder = new NotificationCompat.Builder(contexto, contexto.getString(R.string.favoritos));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel elCanal = new NotificationChannel(contexto.getString(R.string.favoritos), contexto.getString(R.string.añadirFavoritoTitulo), NotificationManager.IMPORTANCE_DEFAULT);

                elCanal.setDescription(contexto.getString(R.string.añadirFavoritoTexto));
                elCanal.enableLights(true);
                elCanal.setLightColor(Color.RED);
                elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                elCanal.enableVibration(true);

                elManager.createNotificationChannel(elCanal);
            }

            elBuilder.setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), android.R.drawable.star_big_on))
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle(contexto.getString(R.string.añadirFavoritoTitulo))
                    .setContentText(contexto.getString(R.string.añadirFavoritoTexto))
                    .setSubText(contexto.getString(R.string.añadirFavoritoSubtexto))
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true);
        }
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // Método que define que debe mostrar una fila de la lista y las acciones correspondientes a cada elemento
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fila_buscar,null);
        ImageView img = (ImageView) view.findViewById(R.id.imagen);
        TextView nomUsuario = (TextView) view.findViewById(R.id.nombreUsuario2);
        TextView nomOferta = (TextView) view.findViewById(R.id.nombreOferta3);
        ImageView contactar = (ImageView) view.findViewById(R.id.contactar);
        ImageView añadirFavoritos = (ImageView) view.findViewById(R.id.añadirFavoritos);

        Oferta oferta = lista.get(i);
        Usuario usuario = oferta.getUsuario();

        String nombreUsuario = usuario.getNombre();
        String nombreOferta = oferta.getNombre();

        Bitmap imageBitmap = usuario.getImagenBitmap();
        if (imageBitmap!=null) {
            img.setImageBitmap(imageBitmap);
        }
        else {
            img.setImageResource(usuario.getImagenDefault());
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPerfil(nombreUsuario);
            }
        });
        nomUsuario.setText(nombreUsuario);
        nomUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPerfil(nombreUsuario);
            }
        });
        nomOferta.setText(nombreOferta);
        contactar.setImageResource(android.R.drawable.sym_action_chat);
        contactar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(contexto, ActivityChatear.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("nombre", nombreUsuario);
                contexto.startActivity(i);
            }
        });
        if(!favoritos) {
            ArrayList<Oferta> favoritas = CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual().getFavoritas().getLista();
            boolean enc = false;
            int cont = 0;
            while(!enc && cont<favoritas.size()) {
                Oferta favorita = favoritas.get(cont);
                if(favorita.getUsuario().getNombre().equals(nombreUsuario) && favorita.getNombre().equals(nombreOferta)) {
                    enc = true;
                }
                else {
                    cont++;
                }
            }
            if (enc) {
                añadirFavoritos.setImageResource(android.R.drawable.btn_star_big_on);
            }
            else {
                añadirFavoritos.setImageResource(android.R.drawable.btn_star_big_off);
            }
            añadirFavoritos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Usuario usuario = CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual();
                    ArrayList<Oferta> favoritas = usuario.getFavoritas().getLista();
                    if(!favoritas.contains(oferta)) {
                        favoritas.add(oferta);
                        oferta.getDemandantes().add(usuario);
                        elManager.notify(1, elBuilder.build());
                        añadirFavoritos.setImageResource(android.R.drawable.btn_star_big_on);
                    }
                    else {
                        favoritas.remove(oferta);
                        oferta.getDemandantes().remove(usuario);
                        añadirFavoritos.setImageResource(android.R.drawable.btn_star_big_off);
                    }
                }
            });
        }
        else {
            añadirFavoritos.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    // Método que lanza la actividad que muestra el perfil de un usuario
    private void mostrarPerfil(String nombreUsuario) {
        Intent i = new Intent (contexto, ActivityMostrarPerfil.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("nombre", nombreUsuario);
        contexto.startActivity(i);
    }
}
