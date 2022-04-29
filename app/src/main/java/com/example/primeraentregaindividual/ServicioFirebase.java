package com.example.primeraentregaindividual;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

// Servicio que entabla la conexión con el servidor de Firebase y recibe las notificaciones
public class ServicioFirebase extends FirebaseMessagingService {

    // Constructora de la clase
    public ServicioFirebase() {}

    // Método que se ejecuta cuando la aplicación está en primer plano o el mensaje solo contiene datos
    // y que genera una notificación
    public void onMessageReceived(RemoteMessage remoteMessage) {
        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "IdCanal");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal",
                    NotificationManager.IMPORTANCE_DEFAULT);

            elCanal.setDescription("Descripción del canal");
            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(true);

            elManager.createNotificationChannel(elCanal);
        }

        Intent i = new Intent(this,ActivityIdentificarse.class);
        PendingIntent intentEnNot = PendingIntent.getActivity(this, 0, i, 0);

        elBuilder.setSmallIcon(R.drawable.app_icon)
                .setContentTitle("¿Estás listo para ofrecer empleos o servicios?")
                .setContentText("¡No pierdas más tiempo, empieza ya!")
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true)
                .setContentIntent(intentEnNot);

        elManager.notify(1, elBuilder.build());
    }

    // Método que se ejecuta cuando se instala la app o se borran los datos,
    // que genera un nuevo token asociado al dispositivo y lo guarda en la base de datos remota
    // y establece una alarma que lanza un servicio que envia un mensaje al dispositivo del usuario cada día a las 12 del mediodía
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        OneTimeWorkRequest trabajoPuntual = BBDD.getBBDD(this).insertToken(s);
        WorkManager.getInstance(this).enqueue(trabajoPuntual);

        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.HOUR_OF_DAY, 12);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);

        Intent i = new Intent(getApplicationContext(), ServiceEnviarMensaje.class);
        PendingIntent i2 = PendingIntent.getService(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager gestor = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        gestor.set(AlarmManager.RTC_WAKEUP, calendario.getTimeInMillis(), i2);
    }
}
