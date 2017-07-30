package nl.mranderson.sittingapp.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import nl.mranderson.sittingapp.common.Utils;
import nl.mranderson.sittingapp.splash.SplashActivity;

public class PushActionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            boolean stopTimer = getIntent().getBooleanExtra("stopTimer", false);
            if (stopTimer) {
                startMainScreen();
                Utils.logFirebaseEvent("NOTIFICATION_STOP_TIMER", "NOTIFICATION");
            } else {
                startMainScreen();
                Utils.logFirebaseEvent("NOTIFICATION_STOP_TIMER", "NOTIFICATION");
            }
        }
        finish();
    }

    private void startMainScreen() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }
}
