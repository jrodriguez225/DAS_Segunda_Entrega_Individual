package com.example.primeraentregaindividual;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

// Clase genérica que crea y muestra los diálogos
public class ClaseDialogo extends DialogFragment {
    private int codigo; // Código que diferencia los distintos diálogos que puede crear una actividad
    private String titulo; // Título del diálogo
    private String mensaje; // Mensaje del diálogo
    private String textPositive; // Texto que aparece en el positive button del diálogo
    private String textNegative; // Texto que aparece en el negative button del diálogo
    private String textNeutral; // Texto que aparece en el neutral button del diálogo
    private ListenerDialogo miListener; // Listener que implementa la interfaz definida en la clase

    // Interfaz que define los métodos que tiene que implementar el listener del diálogo
    public interface ListenerDialogo {
        void alPulsarBoton(int codigo, String boton);
    }

    // Constructora que inicializa los atributos de la clase
    public ClaseDialogo(int pcodigo, String ptitulo, String pmensaje, String ptextPositive, String ptextNegative, String ptextNeutral) {
        codigo = pcodigo;
        titulo = ptitulo;
        mensaje = pmensaje;
        textPositive = ptextPositive;
        textNegative = ptextNegative;
        textNeutral = ptextNeutral;
    }

    // Al crear un diálogo se establece que el listener es la actividad
    // y se establecen los atributos y las acciones correspondientes a cada botón mediante el builder
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        miListener = (ListenerDialogo) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(titulo!=null) {
            builder.setTitle(titulo);
        }
        if(mensaje!=null) {
            builder.setMessage(mensaje);
        }

        if(textPositive!=null) {
            builder.setPositiveButton(textPositive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    miListener.alPulsarBoton(codigo, "positive");
                }
            });
        }

        if(textNegative!=null) {
            builder.setNegativeButton(textNegative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    miListener.alPulsarBoton(codigo, "negative");
                }
            });
        }

        if(textNeutral!=null) {
            builder.setNeutralButton(textNeutral, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    miListener.alPulsarBoton(codigo, "neutral");
                }
            });
        }

        return builder.create();
    }
}
