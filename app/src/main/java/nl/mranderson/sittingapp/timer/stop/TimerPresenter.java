package nl.mranderson.sittingapp.timer.stop;


import nl.mranderson.sittingapp.common.UserPreference;
import nl.mranderson.sittingapp.common.Utils;
import nl.mranderson.sittingapp.timer.recognition.RecognitionInteractor;

public class TimerPresenter implements TimerContract.Presenter, RecognitionInteractor.Listener {

    private TimerContract.View view;
    private RecognitionInteractor recognitionInteractor;
    private UserPreference userPreference;
    private TimeNavigation navigation;
    private boolean hasWalkedBefore;

    public TimerPresenter(RecognitionInteractor recognitionInteractor, UserPreference userPreference, TimeNavigation timeNavigation) {
        this.recognitionInteractor = recognitionInteractor;
        this.userPreference = userPreference;
        this.navigation = timeNavigation;
    }

    @Override
    public void attach(TimerContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void onRecognitionConnection() {
        view.onAssistantSays("Movement sensors are activated!");
    }

    @Override
    public void onRecognitionConnectionSuspended() {
        //TODO show this to the user
    }

    @Override
    public void onRecognitionConnectionFailed() {
        view.onAssistantSays("Movement sensors are not working at this moment!");
    }

    @Override
    public void startTimer(int time) {
//        if (userPreference.hasSensorSettingsEnabled()) {
            recognitionInteractor.startSensors(this);
//        }
        navigation.startTimerService(time);
    }

    @Override
    public void onWalkRecognitionTriggered(boolean isWalking) {
        if (isWalking) {
            view.onAssistantSays("I noticed that you are walking. I'll stop the timer!");
            hasWalkedBefore = true;
        } else if (hasWalkedBefore) {
            view.onAssistantSays("Ah, you stopped walking! Let me restart the timer.");
            hasWalkedBefore = false;
        }
    }

    @Override
    public void onTimerTick(long time) {
        view.setCountdownTime(Utils.formatDate(time));
    }

    @Override
    public void onStopTimer() {
//        if (userPreference.hasSensorSettingsEnabled()) {
            recognitionInteractor.stopSensors();
//        }
        navigation.stopTimerService();
    }
}
