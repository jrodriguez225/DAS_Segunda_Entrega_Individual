package com.example.primeraentregaindividual;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

// Clase que recibe un aviso cuando se inicia el dispositivo
// y establece una alarma que lanza un servicio que envía un mensaje al dispositivo del usuario cada día a las 12 del mediodía
public class ElReceiver extends BroadcastReceiver {

    // Método que establece una alarma cada vez que se inicia el dispositivo.
    // Esta alarma lanza un servicio que envia un mensaje al dispositivo del usuario cada día a las 12 del mediodía
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.HOUR_OF_DAY, 12);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);

        Intent i = new Intent(context, ServiceEnviarMensaje.class);
        PendingIntent i2 = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager gestor = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        gestor.set(AlarmManager.RTC_WAKEUP, calendario.getTimeInMillis(), i2);
    }
}
