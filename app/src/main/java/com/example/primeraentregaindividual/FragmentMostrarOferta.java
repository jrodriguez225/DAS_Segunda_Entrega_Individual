package com.example.primeraentregaindividual;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Fragment de la actividad mostrar oferta
public class FragmentMostrarOferta extends Fragment {
    private ListenerFragment listener; // Listener que implementa la interfaz definida en la clase

    // Interfaz que define los métodos que tiene que implementar el listener del fragment
    public interface ListenerFragment{
        void mostrarOferta(View view, String nombreUsuario, String nombreOferta, String descripcion);
    }

    // Al crear el fragment se establece el layout
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mostrar_oferta,container,false);
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // Método que se asegura de que la actividad implementa la interfaz
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (ListenerFragment) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("La clase " + context.toString()
                    + "debe implementar ListenerFragment");
        }
    }

    // Método que invoca el método que tiene que implementar el listener
    public void mostrarOferta(String nombreUsuario, String nombreOferta, String descripcion) {
        listener.mostrarOferta(getView(), nombreUsuario, nombreOferta, descripcion);

        TextView nomOferta2 = getView().findViewById(R.id.nombreOferta4);
        TextView des2 = getView().findViewById(R.id.descripcion3);
        nomOferta2.setText(getString(R.string.nombre));
        des2.setText(getString(R.string.descripcion));
    }
}