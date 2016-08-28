package nl.mranderson.sittingapp.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import org.greenrobot.eventbus.EventBus;

import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.events.WalkingEvent;

public class ActivityRecognitionIntentService extends IntentService {

    public static final int CONFIDENCE_METER_PERCENTAGE = 80;

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
            if (mostProbableName.equals("On Foot") || mostProbableName.equals("On Bicycle") && confidence > CONFIDENCE_METER_PERCENTAGE) {

                UserPreference.setUserWalked(this, true);
                EventBus.getDefault().post(new WalkingEvent(true));

            } else if (mostProbableName.equals("Still") && confidence > CONFIDENCE_METER_PERCENTAGE && UserPreference.getUserWalked(this)) {

                UserPreference.setUserWalked(this, false);
                EventBus.getDefault().post(new WalkingEvent(false));
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