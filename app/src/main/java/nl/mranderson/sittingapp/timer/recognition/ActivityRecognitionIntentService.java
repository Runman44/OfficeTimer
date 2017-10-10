package nl.mranderson.sittingapp.timer.recognition;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.anadeainc.rxbus.Bus;
import com.anadeainc.rxbus.BusProvider;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import nl.mranderson.sittingapp.events.WalkingEvent;

import static com.google.android.gms.location.DetectedActivity.ON_BICYCLE;
import static com.google.android.gms.location.DetectedActivity.ON_FOOT;
import static com.google.android.gms.location.DetectedActivity.RUNNING;
import static com.google.android.gms.location.DetectedActivity.STILL;
import static com.google.android.gms.location.DetectedActivity.WALKING;


public class ActivityRecognitionIntentService extends IntentService {

    private int CONFIDENCE_METER_PERCENTAGE = 80;
    private Bus bus;

    public ActivityRecognitionIntentService() {
        super("ActivityRecognitionIntentService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("sensitivity")) {
            CONFIDENCE_METER_PERCENTAGE = intent.getIntExtra("sensitivity", 80);
        }
        bus = BusProvider.getInstance();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            DetectedActivity detectedActivity = ActivityRecognitionResult.extractResult(intent).getMostProbableActivity();

            int confidence = detectedActivity.getConfidence();
            int type = detectedActivity.getType();
            if ((type == ON_FOOT || type == ON_BICYCLE || type == RUNNING || type == WALKING) && confidence > CONFIDENCE_METER_PERCENTAGE) {
                bus.post(new WalkingEvent(true));
            } else if (type == STILL && confidence > CONFIDENCE_METER_PERCENTAGE) {
                bus.post(new WalkingEvent(false));
            }
        }
    }
}