package com.matteoveroni.wordsremember.dictionary.management.layout;

import android.widget.LinearLayout;

/**
 * Class that hosts all the data related to the layout of the activity view
 *
 * @author Matteo Veroni
 */

public class DictionaryManagementViewLayout {
    public static final String TAG = "ActivityViewLayout";
    public static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    public enum Type {
        SINGLE, TWO_COLUMNS, TWO_ROWS
    }

    private Type type;
    private String mainFragmentTAG;

    public DictionaryManagementViewLayout(Type type, String mainFragmentTAG) {
        this.type = type;
        this.mainFragmentTAG = mainFragmentTAG;
    }

    public String getMainFragmentTAG() {
        return mainFragmentTAG;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DictionaryManagementViewLayout)) return false;

        DictionaryManagementViewLayout that = (DictionaryManagementViewLayout) o;

        if (getType() != that.getType()) return false;
        return getMainFragmentTAG() != null ? getMainFragmentTAG().equals(that.getMainFragmentTAG()) : that.getMainFragmentTAG() == null;
    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + (getMainFragmentTAG() != null ? getMainFragmentTAG().hashCode() : 0);
        return result;
    }
}
