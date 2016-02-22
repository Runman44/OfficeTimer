package nl.mranderson.sittingapp.fragment;

import nl.mranderson.sittingapp.R;

/**
 * Created by MrAnderson1 on 20/02/16.
 */
public enum TutorialPagerEnum {

    STEP1(R.string.messages_hint_1, R.layout.fragment_info_1),
    STEP2(R.string.messages_hint_2, R.layout.fragment_info_2),
    STEP3(R.string.messages_hint_3, R.layout.fragment_info_3),
    STEP4(R.string.messages_hint_3, R.layout.fragment_info_4);

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