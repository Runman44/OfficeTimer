package nl.mranderson.sittingapp.info;

import nl.mranderson.sittingapp.common.Constants;
import nl.mranderson.sittingapp.common.Utils;

class InfoPresenter implements InfoContract.Presenter {

    private InfoContract.View view;
    private InfoFragmentNavigation navigation;

    public InfoPresenter(InfoFragmentNavigation navigation) {
        this.navigation = navigation;
    }

    @Override
    public void attach(InfoContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void onTutorialTapped() {
        Constants.INSTANCE.setSHOW_TUTORIAL(true);
//        MaterialIntroUtils.generateViewIdList();
//        Toast.makeText(getActivity(), "Tutorial Enabled", Toast.LENGTH_SHORT).show();

        Utils.logFirebaseEvent("TUTORIAL", "BUTTON");
    }

    @Override
    public void onRatingTapped() {
        navigation.startPlayStore();

        Utils.logFirebaseEvent("RATE", "BUTTON");
    }

    @Override
    public void onContactTapped() {
        navigation.startMail();

        Utils.logFirebaseEvent("MAIL", "BUTTON");
    }
}
