package nl.mranderson.sittingapp.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import nl.mranderson.sittingapp.R;

public class AssistantView extends FrameLayout {

    private TypeWriter typeWriter;

    public AssistantView(@NonNull Context context) {
        super(context);
        init();
    }

    public AssistantView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AssistantView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AssistantView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_assisstant, this);
        this.typeWriter = (TypeWriter) findViewById(R.id.tv);
        this.typeWriter.setCharacterDelay(50);
    }

    public void animateText(String text) {
        this.typeWriter.setText("");
        this.typeWriter.animateText(text);
    }
}
