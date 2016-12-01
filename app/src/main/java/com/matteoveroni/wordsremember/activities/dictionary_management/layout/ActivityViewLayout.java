package com.matteoveroni.wordsremember.activities.dictionary_management.layout;

import android.widget.LinearLayout;

/**
 * Class that hosts all the data related to the layout of the activity view
 *
 * @author Matteo Veroni
 */

public class ActivityViewLayout {
    public static final String TAG = "ActivityViewLayoutTag";
    public static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    private Type activityLayoutType;
    private String mainFragmentTAG;

    public enum Type {
        SINGLE, TWO_COLUMNS, TWO_ROWS
    }

    public ActivityViewLayout(Type activityLayoutType, String mainFragmentTAG) {
        this.activityLayoutType = activityLayoutType;
        this.mainFragmentTAG = mainFragmentTAG;
    }

    public String getMainFragmentTAG() {
        return mainFragmentTAG;
    }

    public void setMainFragmentTAG(String mainFragmentTAG) {
        this.mainFragmentTAG = mainFragmentTAG;
    }

    public Type getActivityLayoutType() {
        return activityLayoutType;
    }

    public void setActivityLayoutType(Type activityLayoutType) {
        this.activityLayoutType = activityLayoutType;
    }
}
