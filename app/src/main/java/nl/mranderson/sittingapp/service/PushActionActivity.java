package nl.mranderson.sittingapp.service;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.Utils;
import nl.mranderson.sittingapp.activity.MainActivity;
import nl.mranderson.sittingapp.events.CounterEvent;
import nl.mranderson.sittingapp.events.TimerState;

public class PushActionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().post(new CounterEvent(CounterEvent.CounterAction.STOPPED));
        UserPreference.setTimerStatus(this, TimerState.STOPPED.toString());

        startActivity(new Intent(this, MainActivity.class));

        Utils.logFirebaseEvent("NOTIFICATION_STOP_TIMER");
        finish();
    }
}
