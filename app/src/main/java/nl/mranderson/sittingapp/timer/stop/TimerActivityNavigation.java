package nl.mranderson.sittingapp.timer.stop;

import android.app.Activity;
import android.content.Intent;

import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.common.Navigation;
import nl.mranderson.sittingapp.timer.service.TimerService;

class TimerActivityNavigation implements TimeNavigation, Navigation {

    private final Activity context;

    public TimerActivityNavigation(Activity activity) {
        this.context = activity;
    }

    @Override
    public void startTimerService(int time) {
        Intent timerServiceIntent = new Intent(context, TimerService.class);
        timerServiceIntent.putExtra("time", time);
        context.startService(timerServiceIntent);
    }

    @Override
    public void stopTimerService() {
        Intent timerServiceIntent = new Intent(context, TimerService.class);
        context.stopService(timerServiceIntent);
    }

    @Override
    public void closeScreen() {
        context.finish();
        context.overridePendingTransition(R.anim.push_down_from_up, R.anim.push_out_to_bottom);
    }
}
