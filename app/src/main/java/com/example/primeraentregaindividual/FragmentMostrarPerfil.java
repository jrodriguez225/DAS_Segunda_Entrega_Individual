package com.example.primeraentregaindividual;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Fragment de la actividad mostrar perfil
public class FragmentMostrarPerfil extends Fragment {
    private ListenerFragment listener; // Listener que implementa la interfaz definida en la clase

    // Interfaz que define los métodos que tiene que implementar el listener del fragment
    public interface ListenerFragment{
        void mostrarEmpleosServicios(View view);
    }

    // Al crear el fragment se establece el layout
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mostrar_perfil,container,false);
        return v;
    }

    // Al crear la actividad se llama al método que tiene que implementar
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listener.mostrarEmpleosServicios(getView());
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
}