package com.example.primeraentregaindividual;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import java.util.Locale;

// Actividad en la que se establece el tema y el idioma y de la que heredan las demás actividades
public class Activity extends AppCompatActivity {


    // Al crear la actividad se recogen las preferencias y en base a ellas se establece el tema y el idioma
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean oscuro = prefs.getBoolean("temaOscuro", true);
        cambiarTema(oscuro);
        String idioma = prefs.getString("idioma", getString(R.string.es));
        cambiarIdioma(idioma);
    }

    // Método que establece el idioma de la actividad
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void cambiarIdioma(String idioma) {
        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);

        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

    // Método que establece el tema de la actividad
    protected void cambiarTema(boolean oscuro) {
        if (oscuro) {
            this.setTheme(R.style.TemaDAS);
        }
        else {
            this.setTheme(R.style.TemaDASLight);
        }
    }
}
