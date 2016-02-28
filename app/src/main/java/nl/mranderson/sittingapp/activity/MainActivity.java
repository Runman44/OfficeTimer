package nl.mranderson.sittingapp.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nl.mranderson.sittingapp.Constants;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.UserPreference;
import nl.mranderson.sittingapp.fragment.MainFragment;
import nl.mranderson.sittingapp.fragment.TimerFragment;
import nl.mranderson.sittingapp.fragment.TutorialFragment;

public class MainActivity extends AppCompatActivity {

    //TODO add images
    //TODO add text
    //TODO Tablet !         //TODO just make a different layout folder for tablets with 350dp for the circle or even more !
    //TODO Start making the graph
    //TODO start making the service
    //TODO start making a settings for the service
    //TODO make payment screen
    //TODO make the payment with google
    //TODO version 1.6                                  MAY?
    //TODO do refactoring and testing
    //TODO version 2.0                                  MAY?
    //TODO change store text
    //TODO change screenshots
    //TODO create video?
    //TODO release version 3.0 to public                JUNE?

    //TODO Bug Mariska? FIXED - TEST
    //TODO Play Service not Available - pop-up with link to playstore - TEST

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = this.getSharedPreferences(UserPreference.MY_PREFS_NAME, MODE_PRIVATE);
        Boolean introShown = prefs.getBoolean("introShown", false);

        if (Constants.IS_TIMER_SERVICE_RUNNING) {
            TimerFragment fragment = new TimerFragment();
            getFragmentManager().beginTransaction().add(R.id.fragment, fragment, "timer").commit();
        } else {
            if (!introShown) {
                TutorialFragment fragment = new TutorialFragment();
                getFragmentManager().beginTransaction().add(R.id.fragment, fragment, "tutorial").commit();
                UserPreference.setIntroShown(this, true);
            } else if (getFragmentManager().findFragmentByTag("main") == null) {
                MainFragment fragment = new MainFragment();
                getFragmentManager().beginTransaction().add(R.id.fragment, fragment, "main").commit();
            }
        }
    }

    public void onStartMain(View view) {
        Fragment newFragment = new MainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, newFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() != 0) {
            if(Constants.IS_TIMER_SERVICE_RUNNING){
                // Cancel the current notification.
                NotificationManager mNotifyMgr =
                        (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
                mNotifyMgr.cancel(Constants.NOTIFICATION_GET_WALKING);

                // Stop the service
                this.sendBroadcast(new Intent(Constants.COUNTDOWN_STOP_BROADCAST));
            }
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
