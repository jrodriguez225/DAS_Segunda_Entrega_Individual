package com.example.primeraentregaindividual;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

// Servicio que envía un mensaje recordatorio al dispositivo
public class ServiceEnviarMensaje extends LifecycleService {

    // Método que ejecuta el servicio que envía el mensaje de manera asíncrona
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lanzarNotificacion();

        OneTimeWorkRequest trabajoPuntual = BBDD.getBBDD(this).enviarMensaje();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(trabajoPuntual.getId())
                .observe(this, status -> {
                    if (status != null && status.getState().isFinished()) {
                        stopSelf();
                    }
                });
        WorkManager.getInstance(this).enqueue(trabajoPuntual);

        return super.onStartCommand(intent, flags, startId);
    }

    // Método que lanza una notificación para que el servicio se pueda ejecutar sin tener la aplicación en primer plano en ciertas versiones
    public void lanzarNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager elmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel canalservicio = new NotificationChannel("IdCanal",
                    "NombreCanal",NotificationManager.IMPORTANCE_DEFAULT);
            elmanager.createNotificationChannel(canalservicio);
            Notification.Builder builder = new Notification.Builder(this, "IdCanal")
                    .setContentTitle(getString(R.string.app_name))
                    .setAutoCancel(false);
            Notification notification = builder.build();
            startForeground(1, notification);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
