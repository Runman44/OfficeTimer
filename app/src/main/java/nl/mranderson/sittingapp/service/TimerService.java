package nl.mranderson.sittingapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.activity.MainActivity;
import nl.mranderson.sittingapp.events.CounterEvent;
import nl.mranderson.sittingapp.events.TimeEvent;

public class TimerService extends Service {

    private CountDownTimer countDownTimer;
    private int counterTime;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int time = (int) intent.getExtras().get("time");

            startCountDown(time);
            startForeground(666, createForegroundNotification());

            counterTime = UserPreference.getCounterTime(this);

            EventBus.getDefault().register(this);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void startCountDown(final int time) {

        countDownTimer = new CountDownTimer(((time * 1000) * 60), 1000) {

            public void onTick(long millisUntilFinished) {
                EventBus.getDefault().post(new TimeEvent(millisUntilFinished));
            }

            public void onFinish() {
                sendNotification();
                startCountDown(time);
            }
        };
        countDownTimer.start();
    }

    private void stopTimerService() {
        disableCountDown();

        EventBus.getDefault().unregister(this);
        TimerService.this.stopForeground(true);
        TimerService.this.stopSelf();
    }

    @Subscribe
    public void onCounterEvent(CounterEvent event) {
        switch (event.status) {
            case RESTARTED:
                if (countDownTimer == null)
                    startCountDown(counterTime);
                break;
            case STOPPED:
                stopTimerService();
                break;
            case PAUSED:
                disableCountDown();
                break;
            default:
                break;
        }
    }


    private void disableCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private Notification createForegroundNotification() {
        final Intent nextIntent = new Intent(this, MainActivity.class);
        PendingIntent pNextIntent = PendingIntent.getActivity(this, 0,
                nextIntent, 0);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_directions_walk_white_36dp)
                        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setOngoing(true)
                        .setContentText(getString(R.string.notification_blue))
//                        .addAction(new android.support.v4.app.NotificationCompat.Action(R.drawable.ic_notification_icon, "Stop", new PendingIntent(new Intent(this, MainActivity.class))))
                        .setContentIntent(pNextIntent);

        return mBuilder.build();
    }

    private void sendNotification() {
        Boolean light = UserPreference.getLightSettings(this);
        Boolean vibration = UserPreference.getVibrationSettings(this);
        Boolean sound = UserPreference.getSoundSettings(this);
        String music = UserPreference.getToneSettings(this);

        final Intent nextIntent = new Intent(this, MainActivity.class);
        PendingIntent pNextIntent = PendingIntent.getActivity(this, 0,
                nextIntent, 0);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_directions_walk_white_36dp)
                        .setColor(ContextCompat.getColor(this, R.color.red))
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_red))
                        .setAutoCancel(true)
                        .setContentIntent(pNextIntent);

        //Vibration
        if (vibration)
            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        //LED
        if (light)
            mBuilder.setLights(Color.BLUE, 3000, 3000);

        //Tone
        if (sound)
            mBuilder.setSound(Uri.parse(music));

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(Constants.NOTIFICATION_GET_WALKING, mBuilder.build());
    }
}
