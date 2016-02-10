package nl.mranderson.sittingapp.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import java.util.concurrent.TimeUnit;

import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.custom.CircularSeekBar;
import nl.mranderson.sittingapp.service.ActivityRecognitionIntentService;
import nl.mranderson.sittingapp.service.TimerService;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimerFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView countDownText;
    private TextView messageText;

    private GoogleApiClient mGApiClient;
    private boolean USER_WALKED = false;
    private long millisUntilFinished;
    private BroadcastReceiver timerCountdownReceiver;
    private BroadcastReceiver sensorReceiver;


    public TimerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        countDownText = (TextView) getActivity().findViewById(R.id.countdown);
        messageText = (TextView) getActivity().findViewById(R.id.messageText);
        Button stopButton = (Button) getActivity().findViewById(R.id.bStop);
        stopButton.setOnClickListener(this);

        //TODO crash here
        int time = Constants.TIMER_SELECTED_TIME;
        final int millisStarted = (time * 1000) * 60;

        ImageButton bUpgrade = (ImageButton) getActivity().findViewById(R.id.bUpgrade);
        bUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Not in here yet!", Toast.LENGTH_SHORT).show();
            }
        });

        CircularSeekBar circularSeekbar = (CircularSeekBar) getActivity().findViewById(R.id.seekBar2);
        circularSeekbar.initDrawable(R.drawable.stickman_walk);
        circularSeekbar.hideProgressMarker();
        circularSeekbar.stopTouching();
        circularSeekbar.invalidate();

        if (!Constants.IS_TIMER_SERVICE_RUNNING) {
            startTimerService(time);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences(UserPreference.MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        Boolean sensor = prefs.getBoolean("sensor", true);
        if (sensor)
            startSensors();

        timerCountdownReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras() != null) {
                    millisUntilFinished = intent.getLongExtra("countdown", 0);

                    countDownText.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                    ));

                    setMessages(millisStarted, millisUntilFinished);

                }
            }
        };
        getActivity().registerReceiver(timerCountdownReceiver, new IntentFilter(Constants.COUNTDOWN_TIME_BROADCAST));
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

    private void stopTimerService() {
        getActivity().sendBroadcast(new Intent(Constants.COUNTDOWN_STOP_BROADCAST));
    }

    private void startTimerService(int time) {
        Intent timerServiceIntent = new Intent(getActivity(), TimerService.class);
        timerServiceIntent.putExtra("time", time);
        getActivity().startService(timerServiceIntent);
    }

    @Override
    public void onClick(View v) {
        // Cancel the current notification.
        NotificationManager mNotifyMgr =
                (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(Constants.NOTIFICATION_GET_WALKING);

        // Stop the service
        stopTimerService();

        // Replace fragment
        Fragment newFragment = new MainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, newFragment);
        transaction.commit();
    }

    private void startSensors() {
        //Check Google Play Service Available
        if (isPlayServiceAvailable()) {
            mGApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(ActivityRecognition.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            //Connect to Google API
            mGApiClient.connect();
        } else {
            //TODO do something else?
            Toast.makeText(getActivity(), "Google Play Service not Available", Toast.LENGTH_LONG).show();
        }

        sensorReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getStringExtra("activity").equals("On Foot") || intent.getStringExtra("activity").equals("On Bicycle") && intent.getExtras().getInt("confidence") == 100) {
                    messageText.setText(R.string.messages_moving);
                    USER_WALKED = true;

                    //TODO dont stop the service but only the countdown.
                    getActivity().sendBroadcast(new Intent(Constants.COUNTDOWN_STOP_TIMER_BROADCAST));
                } else if (intent.getStringExtra("activity").equals("Still") && intent.getExtras().getInt("confidence") == 100) {
                    if (USER_WALKED) {
                        USER_WALKED = false;
                        //messageText.setText(R.string.messages_sitting);
                        //TODO dont start the service but do a restart !
                        getActivity().sendBroadcast(new Intent(Constants.COUNTDOWN_RESTART_BROADCAST));
                    }
                }
            }
        };

        getActivity().registerReceiver(sensorReceiver, new IntentFilter(Constants.SENSOR_BROADCAST));
    }

    //Check for Google play services available on device
    private boolean isPlayServiceAvailable() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) == ConnectionResult.SUCCESS;
    }

    // Connection callback from play service.
    @Override
    public void onConnected(Bundle bundle) {
        Intent i = new Intent(getActivity(), ActivityRecognitionIntentService.class);
        PendingIntent mActivityRecongPendingIntent = PendingIntent
                .getService(getActivity(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGApiClient, 0, mActivityRecongPendingIntent);
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
    public void onDestroy() {
        super.onDestroy();

        //TODO unregister
        getActivity().unregisterReceiver(timerCountdownReceiver);
        getActivity().unregisterReceiver(sensorReceiver);

        if (mGApiClient != null)
            mGApiClient.disconnect();
    }
}
