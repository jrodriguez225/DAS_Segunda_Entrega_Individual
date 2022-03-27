package com.example.primeraentregaindividual;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

// Fragment de las preferencias
public class FragmentPreferencias extends PreferenceFragmentCompat {

    // Al crear las preferencias se establece su configuración mediante un xml
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.conf_preferencias);
    }
}
