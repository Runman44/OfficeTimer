package nl.mranderson.sittingapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.activity.MainActivity;

/**
 * Created by MrAnderson1 on 02/12/15.
 */
public class TimerService extends Service {

    private CountDownTimer countDownTimer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final int time = (int) intent.getExtras().get("time");

            startCountDown(time);
            startForeground(666, createForegroundNotification());

            Constants.IS_TIMER_SERVICE_RUNNING = true;

            BroadcastReceiver restartServiceReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    restartTimerService(time);
                }
            };
            registerReceiver(restartServiceReceiver, new IntentFilter(Constants.COUNTDOWN_RESTART_BROADCAST));

            BroadcastReceiver stopServiceReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    stopTimerService();
                }
            };
            registerReceiver(stopServiceReceiver, new IntentFilter(Constants.COUNTDOWN_STOP_BROADCAST));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void startCountDown(final int time) {
        final Intent bi = new Intent(Constants.COUNTDOWN_TIME_BROADCAST);

        countDownTimer = new CountDownTimer(((time * 1000) * 60), 1000) {

            public void onTick(long millisUntilFinished) {
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);
            }

            public void onFinish() {
                sendNotification();
                Intent timerServiceIntent = new Intent(TimerService.this, TimerService.class);
                timerServiceIntent.putExtra("time", time);
                startService(timerServiceIntent);
            }
        };
        countDownTimer.start();
    }

    private void restartTimerService(int time) {
        stopTimerService();
        Intent timerServiceIntent = new Intent(TimerService.this, TimerService.class);
        timerServiceIntent.putExtra("time", time);
        startService(timerServiceIntent);
    }

    private void stopTimerService() {
        Constants.IS_TIMER_SERVICE_RUNNING = false;
        countDownTimer.cancel();
        TimerService.this.stopForeground(true);
        TimerService.this.stopSelf();
    }

    private Notification createForegroundNotification() {
        final Intent nextIntent = new Intent(this, MainActivity.class);
        nextIntent.putExtra("show_timer", true);
        PendingIntent pNextIntent = PendingIntent.getActivity(this, 0,
                nextIntent, 0);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.stickman_walk)
                        .setColor(getResources().getColor(R.color.colorPrimary, null))
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setOngoing(true)
                        .setContentText("Timer is running!")
                        .setContentIntent(pNextIntent);

        return mBuilder.build();
    }

    private void sendNotification() {

        SharedPreferences prefs = this.getSharedPreferences(UserPreference.MY_PREFS_NAME, this.MODE_PRIVATE);
        Boolean light = prefs.getBoolean("light", true);
        Boolean vibration = prefs.getBoolean("vibration", true);
        Boolean sound = prefs.getBoolean("sound", true);

        final Intent nextIntent = new Intent(this, MainActivity.class);
        nextIntent.putExtra("show_timer", true);
        PendingIntent pNextIntent = PendingIntent.getActivity(this, 0,
                nextIntent, 0);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.stickman_walk)
                        .setColor(getResources().getColor(R.color.red, null))
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText("Stand Up !")
                        .setContentIntent(pNextIntent);

        //Vibration
        if (vibration)
            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        //LED
        if (light)
            mBuilder.setLights(Color.BLUE, 3000, 3000);

        //Ton
        if (sound)
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(Constants.NOTIFICATION_GET_WALKING, mBuilder.build());
    }
}
