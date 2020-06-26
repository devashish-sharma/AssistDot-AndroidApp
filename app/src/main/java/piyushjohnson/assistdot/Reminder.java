package piyushjohnson.assistdot;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.logging.Handler;

public class Reminder extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        final Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ringtone.stop();
            }
        },7000);

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                ReminderService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}