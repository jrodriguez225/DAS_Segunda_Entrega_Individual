package com.example.primeraentregaindividual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;

// Base de datos local de la aplicación
public class BBDD extends SQLiteOpenHelper {

    private static BBDD bbdd; // La clase es una MAE (instancia única) por lo que se hace referencia a la clase mediante un atributo estático
    private static Context context; // Contexto de la actividad

    // Constructora de la base de datos
    private BBDD(@Nullable Context context, @Nullable String name,
                @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Método estático que devuelve la instancia de la clase
    public static BBDD getBBDD(Context pcontext) {
        if (bbdd==null) {
            bbdd = new BBDD(pcontext, "app.sqlite", null, 1);
        }
        context = pcontext;
        return bbdd;
    }

    // Al crear la base de datos se genera su estructura, es decir,
    // se crean las tablas que la forman
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Usuarios (" +
                "'Nombre' VARCHAR(255) PRIMARY KEY, " +
                "'Contraseña' VARCHAR(255) NOT NULL, " +
                "'Correo' VARCHAR(255) NOT NULL, " +
                "'Imagen' VARCHAR(255))"
        );

        sqLiteDatabase.execSQL("CREATE TABLE Ofertas (" +
                "'NombreOferta' VARCHAR(255), " +
                "'NombreUsuario' VARCHAR(255), " +
                "'Descripcion' VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY ('NombreOferta', 'NombreUsuario'), " +
                "FOREIGN KEY ('NombreUsuario') " +
                "REFERENCES Usuarios ('Nombre') " +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE)"
        );

        sqLiteDatabase.execSQL("CREATE TABLE Chats (" +
                "'NombreEmisor' VARCHAR(255), " +
                "'NombreReceptor' VARCHAR(255), " +
                "PRIMARY KEY ('NombreEmisor', 'NombreReceptor'), " +
                "FOREIGN KEY ('NombreEmisor') " +
                "REFERENCES Usuarios ('Nombre') " +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE, " +
                "FOREIGN KEY ('NombreReceptor') " +
                "REFERENCES Usuarios ('Nombre') " +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE)"
        );

        sqLiteDatabase.execSQL("CREATE TABLE Mensajes (" +
                "'NombreEmisor' VARCHAR(255), " +
                "'NombreReceptor' VARCHAR(255), " +
                "'FechaHora' TEXT," +
                "'Texto' TEXT NOT NULL," +
                "PRIMARY KEY ('NombreEmisor', 'NombreReceptor', 'FechaHora'), " +
                "FOREIGN KEY ('NombreEmisor', 'NombreReceptor') " +
                "REFERENCES Chats ('NombreEmisor', 'NombreReceptor') " +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE)"
        );

        sqLiteDatabase.execSQL("CREATE TABLE Favoritas (" +
                "'NombreDemandante' VARCHAR(255), " +
                "'NombreOferta' VARCHAR(255), " +
                "'NombreUsuario' VARCHAR(255), " +
                "PRIMARY KEY ('NombreDemandante', 'NombreOferta', 'NombreUsuario'), " +
                "FOREIGN KEY ('NombreDemandante') " +
                "REFERENCES Usuarios ('Nombre') " +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE, " +
                "FOREIGN KEY ('NombreOferta', 'NombreUsuario') " +
                "REFERENCES Ofertas ('NombreOferta', 'NombreUsuario') " +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Método que hace un select de los usuarios a la base de datos y los añade al catálogo de usuarios
    public void selectCatalogoUsuarios() {
        HashMap<String, Usuario> lista = CatalogoUsuarios.getCatalogoUsuarios().getLista();

        SQLiteDatabase bd = getReadableDatabase();
        String[] campos = new String[] {"Nombre", "Contraseña", "Correo"};
        Cursor cu = bd.query("Usuarios",campos,null,null,null,null,null);
        while (cu.moveToNext()){
            String Nombre = cu.getString(0);

            if (!lista.containsKey(Nombre)) {
                String Contraseña = cu.getString(1);
                String Correo = cu.getString(2);

                Usuario usuario = new Usuario(Nombre, Contraseña, Correo);
                if (cu.getColumnCount()==4) {
                    String Imagen = cu.getString(3);
                    Uri imagenUri = Uri.parse(Imagen);
                    Bitmap imagenBitmap = null;
                    try {
                        imagenBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imagenUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    usuario.setImagenBitmap(imagenBitmap);
                }
                lista.put(Nombre, usuario);
            }
        }
        cu.close();
        bd.close();
    }

    // Método que inserta un usuario en la base de datos
    public void insertUsuario(String nombre, String contraseña, String correo) {
        SQLiteDatabase bd = getWritableDatabase();
        ContentValues nuevo = new ContentValues();
        nuevo.put("Nombre", nombre);
        nuevo.put("Contraseña", contraseña);
        nuevo.put("Correo", correo);
        bd.insert("Usuarios", null, nuevo);
        bd.close();
    }

    // Los métodos restantes relacionados a los demás elementos se pretenden implementar para la segunda entrega
    // La base de datos ha sido lo último que se ha implementado y debido a la urgencia de la entrega no ha dado tiempo a terminarla
    // Pero con los métodos anteriores se demuestra que se tiene el conocimiento necesario para realizarla al completo
    /*public void deleteFavorita(Oferta oferta) {
        SQLiteDatabase bd = getWritableDatabase();
        String[] argumentos = new String[] {CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual().getNombre(), oferta.getNombre(), oferta.getUsuario().getNombre()};
        bd.delete("Favoritas", "NombreDemandante=? AND NombreOferta=? AND NombreUsuario=?", argumentos);
        bd.close();
    }

    public void selectFavoritas() {
        HashMap<String, Oferta> listaOfertas = CatalogoOfertas.getCatalogoOfertas().getLista();
        Usuario demandante = CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual();
        ListaOfertas listaFavoritas = demandante.getFavoritas();

        SQLiteDatabase bd = getReadableDatabase();
        String[] campos = new String[] {"NombreOferta, NombreUsuario"};
        String[] argumentos = new String[] {demandante.getNombre()};
        Cursor cu = bd.query("Favoritas",campos,"NombreDemandante=?",argumentos,null,null,null);
        while (cu.moveToNext()){
            String NombreOferta = cu.getString(0);
            String NombreUsuario = cu.getString(1);

            if (!listaFavoritas.getLista().contains()) {
                String Correo = cu.getString(2);

                Usuario usuario = new Usuario(Nombre, Contraseña, Correo);
                if (cu.getColumnCount()==4) {
                    String Imagen = cu.getString(3);
                    Uri imagenUri = Uri.parse(Imagen);
                    Bitmap imagenBitmap = null;
                    try {
                        imagenBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imagenUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    usuario.setImagenBitmap(imagenBitmap);
                }
                lista.put(Nombre, usuario);
            }
        }
        cu.close();
        bd.close();
    }*/
}
