package nl.mranderson.sittingapp.timer.start;

public interface MainContract {

    interface View {

        void setTimeDisplayText(String timeText);
    }

    interface Presenter {

        void attach(View view);

        void detach();

        void startTimer();

        void onTimePlus();

        void onTimeMinus();
    }

}
