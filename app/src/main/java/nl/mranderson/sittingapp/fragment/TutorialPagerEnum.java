package nl.mranderson.sittingapp.fragment;

import nl.mranderson.sittingapp.R;

public enum TutorialPagerEnum {

    STEP1(R.string.step_1, R.layout.fragment_info_1),
    STEP2(R.string.step_2, R.layout.fragment_info_2),
    STEP3(R.string.step_3, R.layout.fragment_info_3),
    STEP4(R.string.step_4, R.layout.fragment_info_4);

    private int mTitleResId;
    private int mLayoutResId;

    TutorialPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}