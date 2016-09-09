package nl.mranderson.sittingapp.service;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import nl.mranderson.sittingapp.Utils;
import nl.mranderson.sittingapp.activity.MainActivity;
import nl.mranderson.sittingapp.activity.TimerActivity;

public class PushActionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO or do Parent in the manifest !
        Intent[] intents = new Intent[2];
        intents[0] = new Intent(this, MainActivity.class);
        intents[1] = new Intent(this, TimerActivity.class).putExtra("ACTION", "STOP");

        startActivities(intents);

        Utils.logFirebaseEvent("NOTIFICATION_STOP_TIMER", "NOTIFICATION");
        finish();
    }
}
