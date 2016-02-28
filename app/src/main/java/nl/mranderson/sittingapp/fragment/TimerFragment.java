package nl.mranderson.sittingapp.fragment;

import android.app.Fragment;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import java.util.concurrent.TimeUnit;

import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.Utils;
import nl.mranderson.sittingapp.custom.CircularSeekBar;
import nl.mranderson.sittingapp.service.ActivityRecognitionIntentService;
import nl.mranderson.sittingapp.service.TimerService;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimerFragment extends Fragment implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView countDownText;
    private TextView messageText;

    private GoogleApiClient mGApiClient;
    private long millisUntilFinished;
    private BroadcastReceiver timerCountdownReceiver;
    private BroadcastReceiver sensorReceiver;
    private boolean sensor;


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

        int time = Constants.TIMER_SELECTED_TIME;
        final int millisStarted = (time * 1000) * 60;

        ImageButton bBack = (ImageButton) getActivity().findViewById(R.id.bBack);
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel the current notification.
                NotificationManager mNotifyMgr =
                        (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
                mNotifyMgr.cancel(Constants.NOTIFICATION_GET_WALKING);

                // Stop the service
                stopTimerService();

                getActivity().onBackPressed();
            }
        });

        ImageButton bUpgrade = (ImageButton) getActivity().findViewById(R.id.bUpgrade);
        bUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getResources().getString(R.string.upgrade_not_there_temp), Toast.LENGTH_SHORT).show();
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
        sensor = prefs.getBoolean("sensor", true);
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
        getActivity().onBackPressed();
    }

    private void startSensors() {
        //Check Google Play Service Available
        if (Utils.isPlayServiceAvailable(getActivity())) {
            mGApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(ActivityRecognition.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            //Connect to Google API
            mGApiClient.connect();
        } else {
            SharedPreferences prefs = getActivity().getSharedPreferences(UserPreference.MY_PREFS_NAME, getActivity().MODE_PRIVATE);
            Boolean playServiceShown = prefs.getBoolean("playServiceShown", false);
            if (!playServiceShown) {
                //TODO cleanup !
                PlayServiceAlertDialog dialog = new PlayServiceAlertDialog(getActivity());
                dialog.setTitle("Google Play Service");
                dialog.setMessage("You don't have the right Play Service installed.");
                dialog.show();

                Toast.makeText(getActivity(), "Google Play Service not Available", Toast.LENGTH_LONG).show();
                UserPreference.setPlayServiceSettings(getActivity(), true);
            }
        }
    }

    // Connection callback from play service.
    @Override
    public void onConnected(Bundle bundle) {
        Intent i = new Intent(getActivity(), ActivityRecognitionIntentService.class);
        PendingIntent mActivityRecongPendingIntent = PendingIntent
                .getService(getActivity(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGApiClient, 0, mActivityRecongPendingIntent);

        sensorReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                messageText.setText(intent.getStringExtra("message"));
            }
        };

        getActivity().registerReceiver(sensorReceiver, new IntentFilter(Constants.SENSOR_BROADCAST));
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

        getActivity().unregisterReceiver(timerCountdownReceiver);

        //TODO dikke crash here
        if (Utils.isPlayServiceAvailable(getActivity()) && sensor)
            getActivity().unregisterReceiver(sensorReceiver);

        if (mGApiClient != null)
            mGApiClient.disconnect();
    }

}
