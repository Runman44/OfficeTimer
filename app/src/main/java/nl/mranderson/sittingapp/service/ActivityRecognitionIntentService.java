package nl.mranderson.sittingapp.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import org.greenrobot.eventbus.EventBus;

import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.events.WalkingEvent;

import static com.google.android.gms.location.DetectedActivity.ON_BICYCLE;
import static com.google.android.gms.location.DetectedActivity.ON_FOOT;
import static com.google.android.gms.location.DetectedActivity.RUNNING;
import static com.google.android.gms.location.DetectedActivity.STILL;
import static com.google.android.gms.location.DetectedActivity.WALKING;

public class ActivityRecognitionIntentService extends IntentService {

    private static final int CONFIDENCE_METER_PERCENTAGE = 70;

    public ActivityRecognitionIntentService() {
        super("ActivityRecognitionIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            DetectedActivity detectedActivity = ActivityRecognitionResult.extractResult(intent).getMostProbableActivity();

            int confidence = detectedActivity.getConfidence();
            int type = detectedActivity.getType();
            if ((type == ON_FOOT || type == ON_BICYCLE || type == RUNNING || type == WALKING) && confidence > CONFIDENCE_METER_PERCENTAGE) {
                UserPreference.setUserWalked(this, true);
                EventBus.getDefault().post(new WalkingEvent(true));
            } else if (((type == STILL && confidence > CONFIDENCE_METER_PERCENTAGE)) && UserPreference.getUserWalked(this)) {
                UserPreference.setUserWalked(this, false);
                EventBus.getDefault().post(new WalkingEvent(false));
            }
        }
    }
}