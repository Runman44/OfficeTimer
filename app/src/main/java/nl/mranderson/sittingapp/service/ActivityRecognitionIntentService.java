package nl.mranderson.sittingapp.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.R;

public class ActivityRecognitionIntentService extends IntentService {

    public ActivityRecognitionIntentService() {
        super("ActivityRecognitionIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity detectedActivity = result.getMostProbableActivity();

            int confidence = detectedActivity.getConfidence();
            String mostProbableName = getActivityName(detectedActivity.getType());
            if (mostProbableName.equals("On Foot") || mostProbableName.equals("On Bicycle") && confidence > 80) {
                Constants.USER_WALKED = true;
                this.sendBroadcast(new Intent(Constants.COUNTDOWN_STOP_TIMER_BROADCAST));

                final Intent i = new Intent(Constants.SENSOR_BROADCAST);
                i.putExtra("message", getString(R.string.messages_moving));
                i.putExtra("walking", true);
                this.sendBroadcast(i);
            } else if (mostProbableName.equals("Still") && confidence > 80) {
                if (Constants.USER_WALKED) {
                    Constants.USER_WALKED = false;

                    final Intent i = new Intent(Constants.SENSOR_BROADCAST);
                    i.putExtra("walking", false);
                    this.sendBroadcast(i);

                    this.sendBroadcast(new Intent(Constants.COUNTDOWN_RESTART_BROADCAST));
                }
            }
        }
    }

    private String getActivityName(int type) {
        switch (type) {
            case DetectedActivity.IN_VEHICLE:
                return "In Vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "On Bicycle";
            case DetectedActivity.ON_FOOT:
                return "On Foot";
            case DetectedActivity.WALKING:
                return "Walking";
            case DetectedActivity.STILL:
                return "Still";
            case DetectedActivity.TILTING:
                return "Tilting";
            case DetectedActivity.RUNNING:
                return "Running";
            case DetectedActivity.UNKNOWN:
                return "Unknown";
        }
        return "N/A";
    }
}