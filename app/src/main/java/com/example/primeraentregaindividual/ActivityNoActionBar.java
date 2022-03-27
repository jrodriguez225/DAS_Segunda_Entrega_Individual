package com.example.primeraentregaindividual;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.RequiresApi;

// Actividad en la que se establecen las acciones correspondientes a cada elemento de la toolbar y de la que heredan las actividades que disponen de ella
public class ActivityNoActionBar extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Al crear la actividad se establece la estrucutura del menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.definicion_menu,menu);
        return true;
    }

    // Método que establece las acciones correspondientes a cada elemento de la toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.opcion1) {
            Intent i = new Intent (this, ActivityBuscar.class);
            i.putExtra("favoritos", false);
            startActivity(i);
        }
        else if (id==R.id.opcion2) {
            Intent i = new Intent (this, ActivityOfrecer.class);
            startActivity(i);
        }
        else if (id==R.id.opcion3) {
            Intent i = new Intent (this, ActivityBuscar.class);
            i.putExtra("favoritos", true);
            startActivity(i);
        }
        else if (id==R.id.opcion4) {
            Intent i = new Intent (this, ActivityMostrarChats.class);
            startActivity(i);
        }
        else if (id==R.id.opcion5) {
            Usuario usuario = CatalogoUsuarios.getCatalogoUsuarios().getUsuarioActual();
            String nombre = usuario.getNombre();

            Intent i = new Intent (this, ActivityMostrarPerfil.class);
            i.putExtra("nombre", nombre);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    // Método que cambia el tema por uno que no disponga de actionbar
    @Override
    protected void cambiarTema(boolean oscuro) {
        if (oscuro) {
            this.setTheme(R.style.TemaDASNoActionBar);
        }
        else {
            this.setTheme(R.style.TemaDASLightNoActionBar);
        }
    }
}
