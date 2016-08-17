package nl.mranderson.sittingapp;

import android.app.Activity;

import java.util.UUID;

import co.mobiwise.materialintro.MaterialIntroConfiguration;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;

public abstract class MaterialIntroUtils {


    private static String[] viewIdList = new String[10];

    public static MaterialIntroView.Builder getMainTimeText(Activity context) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[0]);
        return builder;
    }

    public static MaterialIntroView.Builder getMainCircleButton(Activity context) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[1]);
        return builder;
    }

    public static MaterialIntroView.Builder getMainStartButton(Activity context) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[6]);
        return builder;
    }

    public static MaterialIntroView.Builder getTimerTimeText(Activity context) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[2]);
        return builder;
    }

    public static MaterialIntroView.Builder getTimerCircleButton(Activity context) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[3]);
        return builder;
    }

    public static MaterialIntroView.Builder getTimerCircleButton2(Activity context) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[4]);
        return builder;
    }

    public static MaterialIntroView.Builder getTimerCircleButton3(Activity context) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[9]);
        return builder;
    }

    public static MaterialIntroView.Builder getTimerStopButton(Activity context) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[5]);
        return builder;
    }

    public static MaterialIntroView.Builder getStarting(Activity context) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[7]);
        return builder;
    }

    public static MaterialIntroView.Builder getStopping(Activity context) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[8]);
        return builder;
    }

    public static MaterialIntroView.Builder getDefaultBuilder(Activity context) {
        return new MaterialIntroView.Builder(context)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.NORMAL)
                .setDelayMillis(350)
                .enableFadeAnimation(true)
                .performClick(true);
    }

    public static MaterialIntroConfiguration getDefaultConfig() {
        MaterialIntroConfiguration config = new MaterialIntroConfiguration();
        return config;
    }

    public static void generateViewIdList() {
        for (int i = 0; i < viewIdList.length; i++) {
            viewIdList[i] = String.valueOf(UUID.randomUUID());
        }
    }
}
