package nl.mranderson.sittingapp.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.view.MaterialIntroView;
import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.MaterialIntroUtils;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.Utils;
import nl.mranderson.sittingapp.custom.CircularSeekBar;
import nl.mranderson.sittingapp.events.CounterEvent;
import nl.mranderson.sittingapp.events.TimeEvent;
import nl.mranderson.sittingapp.events.TimerState;
import nl.mranderson.sittingapp.events.WalkingEvent;
import nl.mranderson.sittingapp.service.ActivityRecognitionIntentService;
import nl.mranderson.sittingapp.service.TimerService;

public class TimerActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.countdown)
    TextView countDownText;
    @BindView(R.id.messageText)
    TextView messageText;
    @BindView(R.id.bStop)
    Button stopButton;
    @BindView(R.id.seekBar2)
    CircularSeekBar circularSeekbar;

    private GoogleApiClient mGApiClient;
    private boolean isFirstStart;
    private int millisStarted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_timer);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        //Ads
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        if (BuildConfig.DEBUG) {
//            Button resumeButton = (Button) this.findViewById(R.id.bRESUME);
//            resumeButton.setOnClickListener(this);
//            resumeButton.setVisibility(View.VISIBLE);
//
//            Button pauseButton = (Button) this.findViewById(R.id.bPAUSE);
//            pauseButton.setOnClickListener(this);
//            pauseButton.setVisibility(View.VISIBLE);
//        }

        circularSeekbar.initDrawable(R.drawable.stickman_sitting_1);
        circularSeekbar.hideProgressMarker();
        circularSeekbar.stopTouching();
        circularSeekbar.invalidate();

        int time = UserPreference.getCounterTime(this);
        millisStarted = (time * 1000) * 60;

        TimerState timerState = TimerState.toApplicationState(UserPreference.getTimerStatus(this));
        if (timerState == TimerState.START) {
            startTimerService(time);
            UserPreference.setTimerStatus(this, TimerState.RUNNING.toString());

            if (UserPreference.getSensorSettings(this))
                startSensors();
        } else if (timerState == TimerState.MOVING) {
            onWalkingEvent(new WalkingEvent(true));
        }

        isFirstStart = UserPreference.getIntroShown(this);
    }

    @Subscribe
    public void onEvent(final TimeEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countDownText.setText(Utils.formatDate(event.time));
                setMessages(millisStarted, event.time);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isFirstStart) {
            showStartTutorial();
            UserPreference.setIntroShown(this, false);
        }

        if (Constants.SHOW_TUTORIAL) {
            Constants.SHOW_TUTORIAL = false;
            showStartTutorial();
        }

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if ("STOP".equals(intent.getExtras().getString("ACTION"))) {
                this.onBackPressed();
            }
        }
    }


    private void setMessages(long millisStarted, long millisUntilFinished) {
        //Starting point
        if (millisUntilFinished > (millisStarted * 0.50)) {
            messageText.setText(R.string.messages_starting);
        }

        //Half way
        if (millisUntilFinished <= (millisStarted * 0.50)) {
            messageText.setText(R.string.messages_middle);
        }

        //End is near!
        if (millisUntilFinished <= (millisStarted * 0.25)) {
            messageText.setText(R.string.messages_end);
        }
    }

    private void startTimerService(int time) {
        Intent timerServiceIntent = new Intent(this, TimerService.class);
        timerServiceIntent.putExtra("time", time);
        this.startService(timerServiceIntent);
    }

    @OnClick({R.id.bStop, R.id.bRESUME, R.id.bPAUSE})
    public void onButtonsClicked(View view) {

        switch (view.getId()) {
            case R.id.bRESUME:
                onWalkingEvent(new WalkingEvent(false));
                break;
            case R.id.bPAUSE:
                onWalkingEvent(new WalkingEvent(true));
                break;
            case R.id.bStop:
                Utils.logFirebaseEvent("STOP_TIMER", "BUTTON");
                onBackPressed();
        }
    }

    private void startSensors() {
        //Check Google Play Service Available
        if (Utils.isPlayServiceAvailable(this)) {
            mGApiClient = new GoogleApiClient.Builder(this)
                    .addApi(ActivityRecognition.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            //Connect to Google API
            mGApiClient.connect();
        }
    }

    // Connection callback from play service.
    @Override
    public void onConnected(Bundle bundle) {
        Intent i = new Intent(this, ActivityRecognitionIntentService.class);
        PendingIntent mActivityRecongPendingIntent = PendingIntent
                .getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        circularSeekbar.initDrawable(R.drawable.stickman_sitting_2);
        circularSeekbar.calculated = false;
        circularSeekbar.invalidate();

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGApiClient, 0, mActivityRecongPendingIntent);
    }

    @Subscribe
    public void onWalkingEvent(final WalkingEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.isWalking) {
                    messageText.setText(getString(R.string.messages_moving));
                    countDownText.setText("-");
                    circularSeekbar.initDrawable(R.drawable.stickman_walk_2);
                    circularSeekbar.calculated = false;
                    circularSeekbar.invalidate();
                    UserPreference.setTimerStatus(getApplication(), TimerState.MOVING.toString());
                    EventBus.getDefault().post(new CounterEvent(CounterEvent.CounterAction.PAUSED));
                } else {
                    circularSeekbar.initDrawable(R.drawable.stickman_sitting_2);
                    circularSeekbar.calculated = false;
                    circularSeekbar.invalidate();
                    UserPreference.setTimerStatus(getApplication(), TimerState.RUNNING.toString());
                    EventBus.getDefault().post(new CounterEvent(CounterEvent.CounterAction.RESTARTED));
                }
            }

        });
    }

    // Connection callback from play service.
    @Override
    public void onConnectionSuspended(int i) {
        //TODO show user this feature doesn't work right now.
    }

    // Connection callback from play service.
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //TODO show user this feature doesn't work right now.
    }

    @Override
    public void onBackPressed() {
        killTimer();

        EventBus.getDefault().unregister(this);
        super.onBackPressed();
    }

    private void killTimer() {
        if (TimerState.toApplicationState(UserPreference.getTimerStatus(this)) == TimerState.RUNNING || TimerState.toApplicationState(UserPreference.getTimerStatus(this)) == TimerState.MOVING) {
            // Cancel the current notification.
            NotificationManager mNotifyMgr =
                    (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
            mNotifyMgr.cancel(Constants.NOTIFICATION_GET_WALKING);

            // Stop the service
            EventBus.getDefault().post(new CounterEvent(CounterEvent.CounterAction.STOPPED));

            UserPreference.setTimerStatus(this, TimerState.STOPPED.toString());

            Intent i = new Intent(this, ActivityRecognitionIntentService.class);
            PendingIntent mActivityRecongPendingIntent = PendingIntent
                    .getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            if (mGApiClient != null) {
                ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGApiClient, mActivityRecongPendingIntent);
                mGApiClient.disconnect();
            }
        }
    }

    private void showStartTutorial() {
        final boolean[] opened1 = {true, true, true, true};
        MaterialIntroView.Builder test = MaterialIntroUtils.getTimerTimeText(this);
        test.setInfoText(getString(R.string.tutorial_timer_time_text))
                .setTarget(countDownText)
                .setFocusType(Focus.MINIMUM)
                .setListener(new MaterialIntroListener() {
                    @Override
                    public void onUserClicked(String s) {
                        if (opened1[0]) {
                            opened1[0] = false;
                            circularSeekbar.initDrawable(R.drawable.stickman_sitting_2);
                            circularSeekbar.calculated = false;
                            circularSeekbar.invalidate();

                            MaterialIntroView.Builder test2 = MaterialIntroUtils.getTimerCircleButton(TimerActivity.this);
                            test2.setInfoText(getString(R.string.tutorial_timer_circle))
                                    .setTarget(circularSeekbar)
                                    .setListener(new MaterialIntroListener() {
                                        @Override
                                        public void onUserClicked(String s) {
                                            if (opened1[1]) {
                                                opened1[1] = false;
                                                MaterialIntroView.Builder test4 = MaterialIntroUtils.getTimerCircleButton2(TimerActivity.this);
                                                test4.setInfoText(getString(R.string.tutorial_timer_circle_2))
                                                        .setTarget(circularSeekbar)
                                                        .setListener(new MaterialIntroListener() {
                                                            @Override
                                                            public void onUserClicked(String s) {
                                                                if (opened1[2]) {
                                                                    opened1[2] = false;
                                                                    circularSeekbar.initDrawable(R.drawable.stickman_sitting_1);
                                                                    circularSeekbar.calculated = false;
                                                                    circularSeekbar.invalidate();

                                                                    MaterialIntroView.Builder test5 = MaterialIntroUtils.getTimerCircleButton3(TimerActivity.this);
                                                                    test5.setInfoText(getString(R.string.tutorial_timer_circle_3))
                                                                            .setTarget(circularSeekbar)
                                                                            .setListener(new MaterialIntroListener() {
                                                                                @Override
                                                                                public void onUserClicked(String s) {
                                                                                    if (opened1[3]) {
                                                                                        opened1[3] = false;
                                                                                        MaterialIntroView.Builder test3 = MaterialIntroUtils.getTimerStopButton(TimerActivity.this);
                                                                                        test3.setInfoText(getString(R.string.tutorial_stop_button))
                                                                                                .setTarget(stopButton)
                                                                                                .setFocusType(Focus.ALL)
                                                                                                .enableDotAnimation(true)
                                                                                                .show();
                                                                                    }
                                                                                }
                                                                            });
                                                                    test5.show();
                                                                }
                                                            }
                                                        });
                                                test4.show();
                                            }
                                        }
                                    });
                            test2.show();
                        }
                    }
                });
        test.show();
    }
}
