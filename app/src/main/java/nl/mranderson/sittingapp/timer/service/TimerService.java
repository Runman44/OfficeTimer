package nl.mranderson.sittingapp.timer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import com.anadeainc.rxbus.Bus;
import com.anadeainc.rxbus.BusProvider;
import com.anadeainc.rxbus.Subscribe;

import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.common.Constants;
import nl.mranderson.sittingapp.common.UserPreference;
import nl.mranderson.sittingapp.events.TimeEvent;
import nl.mranderson.sittingapp.events.WalkingEvent;
import nl.mranderson.sittingapp.push.PushActionActivity;
import nl.mranderson.sittingapp.splash.SplashActivity;

public class TimerService extends Service {

    private CountDownTimer countDownTimer;
    private Bus bus;
    private int time;
    private UserPreference userPreference;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            time = (int) intent.getExtras().get("time");

            bus = BusProvider.getInstance();
            bus.register(this);

            userPreference = new UserPreference(this);

            startCountDown(time);
            startForeground(Constants.INSTANCE.getNOTIFICATION_TIMER_RUNNING(), createForegroundNotification());
        } else {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void startCountDown(final int time) {

        countDownTimer = new CountDownTimer(((time * 1000) * 60), 1000) {
            public void onTick(long millisUntilFinished) {
                tick(millisUntilFinished);
            }

            public void onFinish() {
                sendNotification();
                startCountDown(time);
            }
        };
        countDownTimer.start();
    }

    private void tick(long millisUntilFinished) {
        bus.post(new TimeEvent(millisUntilFinished));
    }

    @Subscribe
    public void onWalking(final WalkingEvent event) {
        if (event.isWalking) {
            sendNotification();
            countDownTimer.cancel();
            userPreference.setUserWalked(true);
        } else if (userPreference.hasUserWalkedBefore()) {
            userPreference.setUserWalked(false);
            countDownTimer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disableCountDown();
        TimerService.this.stopForeground(true);
        bus.unregister(this);
        cancelLocalNotification();
    }

    private void cancelLocalNotification() {
        NotificationManager mNotifyMgr =
                (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(Constants.INSTANCE.getNOTIFICATION_GET_WALKING());
    }

    private void disableCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    //TODO seperate class. or maybe this is not possible for the service :o dont know if application code is still accessable.
    private Notification createForegroundNotification() {
        final Intent clickIntent = new Intent(this, PushActionActivity.class);
        PendingIntent pNextIntent = PendingIntent.getActivity(this, 0,
                clickIntent, 0);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_directions_walk_white_36dp)
                        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setOngoing(true)
                        .setContentText(getString(R.string.notification_blue))
                        .setContentIntent(pNextIntent);

        return mBuilder.build();
    }

    private void sendNotification() {
        Boolean light = userPreference.getLightSettings();
        Boolean vibration = userPreference.getVibrationSettings();
        Boolean sound = userPreference.getSoundSettings();
        Boolean wake = userPreference.getWakeSettings();
        String music = userPreference.getToneSettings();

        final Intent nextIntent = new Intent(this, SplashActivity.class);
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

//        Vibration
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
        mNotifyMgr.notify(99, mBuilder.build());

        // Wake Phone
        if (wake) {
            //TODO other way? what is this feature again? only on this notification?
            PowerManager.WakeLock screenOn = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "example");
//            screenOn.acquire(counterTime);
        }
    }
}
