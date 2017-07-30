package nl.mranderson.sittingapp.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


public class CountdownTextView extends android.support.v7.widget.AppCompatTextView {

    public CountdownTextView(Context context) {
        super(context);
        init();
    }

    public CountdownTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountdownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

    }
}
