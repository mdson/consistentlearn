package madson.ifpe.edu.myapplication;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        displayNotification(context, message);
    }

    @SuppressLint("MissingPermission")
    private void displayNotification(Context context, String message) {
        // Cria um canal de notificação (necessário para dispositivos Android 8.0 e acima)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Notificações";
            String channelId = "canal_notificacoes";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Cria a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "canal_notificacoes")
                //.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Lembrete")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Exibe a notificação
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int notificationId = 1;
        notificationManager.notify(notificationId++, builder.build());
    }
}
