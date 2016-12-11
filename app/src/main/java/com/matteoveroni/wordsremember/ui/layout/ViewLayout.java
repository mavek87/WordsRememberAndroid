package com.matteoveroni.wordsremember.ui.layout;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ViewLayout {
    public static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
//    public static final int B = RelativeLayout.LayoutParams.MATCH_PARENT;

    private ViewLayoutType viewLayoutType = null;
    private String viewLayoutMainFragmentTAG = null;

    public ViewLayout(ViewLayoutType viewLayoutType) {
        this.viewLayoutType = viewLayoutType;
    }

    public void setViewLayoutType(ViewLayoutType viewLayoutType) {
        this.viewLayoutType = viewLayoutType;
    }

    public void setMainFragmentTAG(String viewLayoutMainFragmentTAG) {
        this.viewLayoutMainFragmentTAG = viewLayoutMainFragmentTAG;
    }

    public ViewLayoutType getViewLayoutType() {
        return this.viewLayoutType;
    }

    public String getMainFragmentTAG() {
        return this.viewLayoutMainFragmentTAG;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewLayout)) return false;

        ViewLayout that = (ViewLayout) o;

        return getViewLayoutType() == that.getViewLayoutType() && (viewLayoutMainFragmentTAG != null ? viewLayoutMainFragmentTAG.equals(that.viewLayoutMainFragmentTAG) : that.viewLayoutMainFragmentTAG == null);
    }

    @Override
    public int hashCode() {
        int result = getViewLayoutType().hashCode();
        result = 31 * result + (viewLayoutMainFragmentTAG != null ? viewLayoutMainFragmentTAG.hashCode() : 0);
        return result;
    }
}
