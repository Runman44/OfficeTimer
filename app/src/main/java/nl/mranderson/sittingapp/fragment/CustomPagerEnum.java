package nl.mranderson.sittingapp.fragment;

import nl.mranderson.sittingapp.R;

/**
 * Created by MrAnderson1 on 20/02/16.
 */
public enum CustomPagerEnum {

    RED(R.string.messages_hint_1, R.layout.fragment_info_1),
    BLUE(R.string.messages_hint_2, R.layout.fragment_info_2),
    ORANGE(R.string.messages_hint_3, R.layout.fragment_info_3);

    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
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