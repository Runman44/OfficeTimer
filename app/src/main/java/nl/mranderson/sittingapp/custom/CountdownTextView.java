package nl.mranderson.sittingapp.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import nl.mranderson.sittingapp.common.FontCache;


public class CountdownTextView extends android.support.v7.widget.AppCompatTextView {

    public CountdownTextView(Context context) {
        super(context);
        init(context);
    }

    public CountdownTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CountdownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
//        inflate(getContext(), R.layout.view_countdown, null);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("nunito_bold.ttf", context);
        setTypeface(customFont);
    }
}
