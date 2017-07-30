package nl.mranderson.sittingapp.timer.stop;

public interface TimerContract {

    interface View {
        void onAssistantSays(String message);

        void setCountdownTime(String time);
    }

    interface Presenter {

        void attach(View view);

        void detach();

        void onStopTimer();

        void startTimer(int time);

        void onWalkRecognitionTriggered(boolean isWalking);

        void onTimerTick(long time);
    }

}
