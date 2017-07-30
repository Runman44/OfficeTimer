package nl.mranderson.sittingapp.timer.stop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anadeainc.rxbus.Bus;
import com.anadeainc.rxbus.BusProvider;
import com.anadeainc.rxbus.Subscribe;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import nl.mranderson.sittingapp.BuildConfig;
import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.common.UserPreference;
import nl.mranderson.sittingapp.custom.AssistantView;
import nl.mranderson.sittingapp.events.TimeEvent;
import nl.mranderson.sittingapp.events.WalkingEvent;
import nl.mranderson.sittingapp.timer.recognition.RecognitionInteractor;

public class TimerActivity extends AppCompatActivity implements TimerContract.View {

    private TextView countDownText;
    private Button stopButton;

    private TimerContract.Presenter presenter;
    private int time;
    private Bus bus;
    private AssistantView assistant;

    public static Intent newInstance(Context context) {
        return new Intent(context, TimerActivity.class);
    }

    public static Intent newInstance(Context context, int timerTime) {
        Intent intent = new Intent(context, TimerActivity.class);
        intent.putExtra("time", timerTime);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_timer);
        countDownText = (TextView) findViewById(R.id.countdown);
        stopButton = (Button) findViewById(R.id.bStop);
        assistant = (AssistantView) findViewById(R.id.assistent);
        bus = BusProvider.getInstance();
        bus.register(this);

        setPresenter();
        setListeners();
        setAdvertisement();
        setDebugButtons();
        setAssistant();

        if (getIntent().hasExtra("time")) {
            time = getIntent().getIntExtra("time", 0);
            startTimer();
        }
    }

    private void setAdvertisement() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void startTimer() {
        presenter.startTimer(time);
    }

    private void setListeners() {
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onStopTimer();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
        bus.unregister(this);
    }

    private void setDebugButtons() {
        //TODO perfect for flavor
        if (BuildConfig.DEBUG) {
            Button resumeButton = (Button) this.findViewById(R.id.bRESUME);
            resumeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bus.post(new WalkingEvent(false));
                }
            });
//            resumeButton.setVisibility(View.VISIBLE);

            Button pauseButton = (Button) this.findViewById(R.id.bPAUSE);
            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bus.post(new WalkingEvent(true));
                }
            });
//            pauseButton.setVisibility(View.VISIBLE);
        }
    }

    private void setPresenter() {
        presenter = createPresenter();
        presenter.attach(this);
    }

    private TimerContract.Presenter createPresenter() {
        UserPreference userPreference = new UserPreference(this);
        TimeNavigation timeNavigation = new TimerActivityNavigation(this);
        RecognitionInteractor recognitionInteractor = new RecognitionInteractor(this);
        return new TimerPresenter(recognitionInteractor, userPreference, timeNavigation);
    }

    @Subscribe
    public void onTimerTick(final TimeEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.onTimerTick(event.time);
            }
        });
    }

    @Subscribe
    public void onWalking(final WalkingEvent event) {
        presenter.onWalkRecognitionTriggered(event.isWalking);
    }

    private void setAssistant() {
        assistant.animateText("Enjoy your \"sitting\" for the next minutes!");
    }

    @Override
    public void onAssistantSays(String message) {
        assistant.animateText(message);
    }

    @Override
    public void setCountdownTime(String time) {
        countDownText.setText(time);
    }
}
