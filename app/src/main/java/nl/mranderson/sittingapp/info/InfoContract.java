package nl.mranderson.sittingapp.info;

public interface InfoContract {

    interface View {

    }

    interface Presenter {

        void attach(View view);

        void detach();

        void onTutorialTapped();

        void onRatingTapped();

        void onContactTapped();
    }

}
