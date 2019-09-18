package com.nichetech.smartonsite.benchmark.Common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;
import com.nichetech.smartonsite.benchmark.R;

/**
 * Created by android-2 on 4/22/17.
 */

public class SrvPositioning extends Service {

    private AlarmManager alarmManagerPositioning;
    public PendingIntent pendingIntentPositioning;
    public static int isFirsttime= 0;

    @Override
    public void onCreate() {
        super.onCreate();
        alarmManagerPositioning = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);
        Intent intentToFire = new Intent(
                ReceiverPositioningAlarm.ACTION_REFRESH_SCHEDULE_ALARM);
        intentToFire.putExtra(ReceiverPositioningAlarm.COMMAND,
                ReceiverPositioningAlarm.SENDER_SRV_POSITIONING);
        pendingIntentPositioning = PendingIntent.getBroadcast(this, 0,
                intentToFire, 0);
    }

    @Override
    public void onStart(Intent intent, int startId) {

        try {
            long interval = 1000 * 60;
            int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
            long timetoRefresh = SystemClock.elapsedRealtime();
            if (Utility.isOnline(getApplicationContext())) {
                alarmManagerPositioning.setInexactRepeating(alarmType,
                        timetoRefresh, interval, pendingIntentPositioning);
            } else {
                Utility.showAlert(getApplicationContext(), getString(R.string.msgNoInternet));
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    "error running service: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,
                    "error running service: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        this.alarmManagerPositioning.cancel(pendingIntentPositioning);
    }
}
