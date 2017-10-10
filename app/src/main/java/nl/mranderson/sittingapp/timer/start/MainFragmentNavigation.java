package nl.mranderson.sittingapp.timer.start;

import android.app.Activity;
import android.content.Intent;

import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.common.Navigation;
import nl.mranderson.sittingapp.timer.stop.TimerActivity;

class MainFragmentNavigation implements MainNavigation, Navigation {

    private final Activity context;

    public MainFragmentNavigation(Activity activity) {
        this.context = activity;
    }

    @Override
    public void startTimer(int time) {
        Intent intent = TimerActivity.newInstance(context, time);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_up_from_bottom, R.anim.push_out_to_up);
    }
}
