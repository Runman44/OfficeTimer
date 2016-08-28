package nl.mranderson.sittingapp;

import android.app.Activity;
import android.view.View;

import java.util.UUID;

import co.mobiwise.materialintro.MaterialIntroConfiguration;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;

public abstract class MaterialIntroUtils implements MaterialIntroListener {


    private static String[] viewIdList = new String[10];

    public static MaterialIntroView.Builder getMainTimeText(Activity context, View view, String message) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[0]);
        builder.setTarget(view);
        builder.setInfoText(message);
        return builder;
    }

    public static MaterialIntroView.Builder getMainCircleButton(Activity context, View view, String message) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[1]);
        builder.setTarget(view);
        builder.setInfoText(message);
        return builder;
    }

    public static MaterialIntroView.Builder getMainStartButton(Activity context, View view, String message) {
        MaterialIntroView.Builder builder = getDefaultBuilder(context);
        builder.setUsageId(viewIdList[6]);
        builder.setTarget(view);
        builder.setInfoText(message);
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

//    public void generate(final ArrayList<MaterialIntroView.Builder> tutorialList) {
//
//        for (int position = 0; position < tutorialList.size(); position++) {
//
//            if (tutorialList.get(position + 1) != null) {
//                MaterialIntroView.Builder builder = tutorialList.get(position);
//                builder.setListener(new MaterialIntroListener() {
//                    @Override
//                    public void onUserClicked(String s) {
//                        MaterialIntroView.Builder builder2 = tutorialList.get(position + 1);
//                        builder2.setListener(new MaterialIntroListener() {
//                            @Override
//                            public void onUserClicked(String s) {
//                                MaterialIntroView.Builder builder3 = tutorialList.get(position + 2);
//                                builder3.show();
//                            }
//                        });
//                        builder2.show();
//                    }
//                });
//                builder.show();
//            }
//        }
//    }


////        for (int position = 0; position == tutorialList.size(); position++) {
//            if (position1 < tutorialList.size()) {
//                MaterialIntroView.Builder builder = tutorialList.get(position1);
//
//
//                builder.setListener(generate(tutorialList, position1 + 1));
//                builder.show();
//            }
//
////        }
////            tutorialList.get(i).setListener()

}
