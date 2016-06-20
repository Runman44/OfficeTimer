package nl.mranderson.sittingapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import nl.mranderson.sittingapp.Constants;

//TODO depends on the value. stop would mean - Cancel the notifications and cancel the timer. by doing a broadcast. open the app.
public class PushActionActivity extends Activity {

    private boolean stop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getIntent().hasExtra("stop")) {
             stop = getIntent().getBooleanExtra("stop", false);
        }

        //TODO only clean the RED notification. maybe also in the service to keep it simple.
        if (stop) {
            this.sendBroadcast(new Intent(Constants.COUNTDOWN_STOP_BROADCAST));
            startActivity(new Intent(this, MainActivity.class));
        }

        handleNotification();

    }

    private void handleNotification() {

    }

    @Override
    public void onStart() {
        super.onStart();
        finish();
    }

}
