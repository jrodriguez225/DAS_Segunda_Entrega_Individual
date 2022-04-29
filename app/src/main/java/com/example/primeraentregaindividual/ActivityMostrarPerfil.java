package com.example.primeraentregaindividual;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// Actividad que muestra el perfil del usuario actual o del resto de usuarios
public class ActivityMostrarPerfil extends ActivityNoActionBar implements ClaseDialogo.ListenerDialogo, FragmentMostrarPerfil.ListenerFragment, FragmentMostrarOferta.ListenerFragment {
    private String nombre; // Nombre del usuario
    private String filtro; // Filtro para la lista de ofertas del usuario
    private ArrayList<String> empleos; // Lista de empleos del usuario que se le pasa al adaptador del ListView de empleos
    private ArrayList<String> servicios; // Lista de servicios del usuario que se le pasa al adaptador del ListView de servicios
    private ArrayAdapter eladap; // El adaptador del ListView
    private int ofertaEliminada; // En caso de que se elimine alguna oferta se almacena su posición en la lista
    private ImageView imagen2; // ImageView en la que se muestra la foto de perfil del usuario
    private Usuario usuario; // Usuario

    // Al crear la actividad se establece la toolbar
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_perfil);

        setSupportActionBar(findViewById(R.id.labarra));
    }

    // Método que muestra una oferta una vez es seleccionada de alguna de las ListViews
    private void mostrarOferta(View view) {
        String nombreUsuario = ((TextView)findViewById(R.id.nombre2)).getText().toString();
        String nombreOferta = ((TextView)view).getText().toString();

        Oferta oferta = CatalogoOfertas.getCatalogoOfertas().getLista().get(nombreUsuario + "+" + nombreOferta);

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

    // Método que lanza un dialogo de confirmación para eliminar una oferta de una de las ListViews
    private boolean eliminarOferta(String pfiltro, int i, ArrayAdapter peladap) {
        filtro = pfiltro;
        ofertaEliminada = i;
        eladap = peladap;

        DialogFragment dialogo = new ClaseDialogo(1,getString(R.string.eliminarOfertaTitulo),getString(R.string.eliminarOfertaMensaje),getString(R.string.si),getString(R.string.no),null);
        dialogo.setCancelable(false);
        dialogo.show(getSupportFragmentManager(), "etiqueta");

        return true;
    }

    // Método que muestra un toast informando de que se ha eliminado una oferta
    private void mostrarToast(String texto) {
        LayoutInflater inflater = getLayoutInflater();
        View el_layout = inflater.inflate(R.layout.toastlayout,(ViewGroup) findViewById(R.id.idlayout));
        TextView texto2 = el_layout.findViewById(R.id.texto2);
        texto2.setText(getText(R.string.toastOfertas) + " \"" + texto + "\"");
        Toast toastcustomizado = new Toast(this);
        toastcustomizado.setGravity(Gravity.TOP, 0, 225);
        toastcustomizado.setDuration(Toast.LENGTH_LONG);
        toastcustomizado.setView(el_layout);
        toastcustomizado.show();
    }

    // Método que lanza la actividad en la que se entabla una conversación con el otro usuario
    public void contactar(View v) {
        Intent i = new Intent(this, ActivityChatear.class);
        i.putExtra("nombre", nombre);
        startActivity(i);
    }

    // Al pulsar SI en el diálogo de confirmación que se muestra al intentar eliminar una oferta
    // se elimina la oferta de la lista de ofertas del usuario, del catálogo de ofertas y
    // de la lista de favoritos de los usuarios que la tienen como favorita y se muestra un toast que informa de ello
    // Al pulsar SI en el diálogo de confirmación que se muestra al intentar restablecer la imagen de perfil predeterminada
    // se borra la imagen que hubiera y se establece la de por defecto
    @Override
    public void alPulsarBoton(int codigo, String boton) {
        if(codigo==1) {
            if(boton.equals("positive")) {
                Usuario usuario = CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual();
                ListaOfertas lista = usuario.getOfertas();
                Oferta oferta = lista.filtrarOferta(filtro, ofertaEliminada);

                String texto = oferta.getNombre();
                mostrarToast(texto);

                lista.getLista().remove(oferta);
                CatalogoOfertas.getCatalogoOfertas().getLista().remove(usuario.getNombre() + "+" + texto);
                ArrayList<Usuario> demandantes = oferta.getDemandantes();
                for(int j=0; j<demandantes.size(); j++) {
                    Usuario demandante = demandantes.get(j);
                    demandante.getFavoritas().getLista().remove(oferta);
                }
                if (filtro.equals("empleos")) {
                    empleos.remove(ofertaEliminada);
                }
                else if (filtro.equals("servicios")) {
                    servicios.remove(ofertaEliminada);
                }

                eladap.notifyDataSetChanged();
            }
        }
        if(codigo==2) {
            if(boton.equals("positive")) {
                imagen2.setImageResource(R.drawable.user_icon);
                usuario.setImagenBitmap(null);

                OneTimeWorkRequest trabajoPuntual = BBDD.getBBDD(this).updateImagen("null", nombre);
                WorkManager.getInstance(this).enqueue(trabajoPuntual);
            }
        }
    }

    static final int CODIGO_GALERIA = 1; // Código para recibir la imagen escogida a través de la app
    static final int CODIGO_FOTO = 2; // Código para recibir la imagen sacada a través de la app
    static final int CODIGO_DE_PERMISO = 3; // Código utlizado para pedir permiso al usuario para sacar una foto

    // Método que hace uso de un intent implícito para abrir una app con la que escoger una foto de la galería
    private void seleccionarImagen(){
        Intent elIntentGal = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(elIntentGal, CODIGO_GALERIA);
    }

    // Método que pide permiso al usuario y hace uso de un intent implícito para abrir una app con la que sacar una foto
    private void sacarFotografia() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            //EL PERMISO NO ESTÁ CONCEDIDO, PEDIRLO
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                // MOSTRAR AL USUARIO UNA EXPLICACIÓN DE POR QUÉ ES NECESARIO EL PERMISO

            }
            else{
                //EL PERMISO NO ESTÁ CONCEDIDO TODAVÍA O EL USUARIO HA INDICADO
                // QUE NO QUIERE QUE SE LE VUELVA A SOLICITAR

            }
            //PEDIR EL PERMISO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CODIGO_DE_PERMISO);
        }
        else {
            //EL PERMISO ESTÁ CONCEDIDO, EJECUTAR LA FUNCIONALIDAD
            intentSacarFotografia();
        }
    }

    // Método que recoge la respuesta del usuario respecto a la petición de permiso para sacar una foto
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==CODIGO_DE_PERMISO) {
                // Si la petición se cancela, granResults estará vacío
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISO CONCEDIDO, EJECUTAR LA FUNCIONALIDAD
                    intentSacarFotografia();
                }
                else {
                    // PERMISO DENEGADO, DESHABILITAR LA FUNCIONALIDAD O EJECUTAR ALTERNATIVA

                }
                return;
        }
    }

    // Método que hace uso de un intent implícito para sacar una foto
    private void intentSacarFotografia() {
        Intent elIntentFoto= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(elIntentFoto, CODIGO_FOTO);
    }

    // Método que recoge tanto la imagen sacada como la imagen escogida y en cada caso la establece como foto de perfil
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                Uri uri = null;
                Bitmap bitmap = null;
                if (requestCode == CODIGO_GALERIA) {
                    uri = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                }
                else if (requestCode == CODIGO_FOTO) {
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    File eldirectorio = this.getFilesDir();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    String nombrefichero = "IMG_" + timeStamp + "_";
                    File imagenFich = new File(eldirectorio, nombrefichero + ".jpg");
                    OutputStream os = new FileOutputStream(imagenFich);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();

                    uri = Uri.fromFile(imagenFich);
                }
                imagen2.setImageBitmap(bitmap);
                usuario.setImagenBitmap(bitmap);

                OneTimeWorkRequest trabajoPuntual = BBDD.getBBDD(this).updateImagen(uri.toString(), nombre);
                WorkManager.getInstance(this).enqueue(trabajoPuntual);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método que muestra los empleos y los servicios generados por el usuario, cada uno en su ListView correspondiente
    @Override
    public void mostrarEmpleosServicios(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nombre = extras.getString("nombre");

            imagen2 = findViewById(R.id.imagen2);
            TextView nombre2 = view.findViewById(R.id.nombre2);
            ImageView contactar = view.findViewById(R.id.contactar3);
            ListView listaEmpleos = (ListView) view.findViewById(R.id.listaEmpleos);
            ListView listaServicios = (ListView) view.findViewById(R.id.listaServicios);
            ImageView userIcon = view.findViewById(R.id.userIcon2);
            ImageView cameraIcon = view.findViewById(R.id.cameraIcon);
            ImageView galleryIcon = view.findViewById(R.id.galleryIcon);

            usuario = CatalogoUsuarios.getCatalogoUsuarios().getLista().get(nombre);
            ArrayList<Oferta> ofertas = usuario.getOfertas().getLista();
            empleos = new ArrayList<String>();
            servicios = new ArrayList<String>();
            for (int i=0; i<ofertas.size(); i++) {
                Oferta oferta = ofertas.get(i);
                if(oferta instanceof  Empleo) {
                    empleos.add(oferta.getNombre());
                }
                else if(oferta instanceof  Servicio) {
                    servicios.add(oferta.getNombre());
                }
            }

            ArrayAdapter listaEmpleosAdap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, empleos);
            ArrayAdapter listaServiciosAdap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, servicios);

            Bitmap imageBitmap = usuario.getImagenBitmap();
            if (imageBitmap!=null) {
                imagen2.setImageBitmap(imageBitmap);
            }
            else {
                imagen2.setImageResource(usuario.getImagenDefault());
            }
            nombre2.setText(nombre);
            listaEmpleos.setAdapter(listaEmpleosAdap);
            listaEmpleos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    mostrarOferta(view);
                }
            });
            listaServicios.setAdapter(listaServiciosAdap);
            listaServicios.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    mostrarOferta(view);
                }
            });
            if(nombre.equals(CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual().getNombre())) {
                userIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogFragment dialogo = new ClaseDialogo(2,getString(R.string.restablecerImagenTitulo),getString(R.string.restablecerImagenMensaje),getString(R.string.si),getString(R.string.no),null);
                        dialogo.setCancelable(false);
                        dialogo.show(getSupportFragmentManager(), "etiqueta");
                    }
                });
                cameraIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sacarFotografia();
                    }
                });
                galleryIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seleccionarImagen();
                    }
                });
                contactar.setVisibility(View.INVISIBLE);
                listaEmpleos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        return eliminarOferta("empleos", i, listaEmpleosAdap);
                    }
                });
                listaServicios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        return eliminarOferta("servicios", i, listaServiciosAdap);
                    }
                });
            }
            else {
                userIcon.setVisibility(View.INVISIBLE);
                cameraIcon.setVisibility(View.INVISIBLE);
                galleryIcon.setVisibility(View.INVISIBLE);
            }
        }
    }

    // Método que muestra una oferta una vez es seleccionada del ListView correspondiente
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
