package com.example.primeraentregaindividual;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

// Actividad en la que se establecen las preferencias del usuario
public class ActivityConfigurar extends ActivityNoActionBar {

    // Al crear la actividad se establece la toolbar
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar);

        setSupportActionBar(findViewById(R.id.labarra));
    }

    // Al pulsar la tecla back del dispositivo se finaliza la actividad y
    // se lanza nuevamente la actividad de la pantalla principal de la aplicación
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(this, ActivityMain.class);
        startActivity(i);
    }
}
