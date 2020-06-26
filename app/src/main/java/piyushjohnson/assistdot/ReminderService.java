package piyushjohnson.assistdot;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class ReminderService extends IntentService {

    private NotificationManager alarmNotificationManager;


    public ReminderService() {
        super("ReminderService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sendNotification();
    }

    private void sendNotification()
    {
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Home.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Notify"))
                .setContentText("Subtitle").setAutoCancel(true);
        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
    }
}
