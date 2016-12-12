package com.matteoveroni.wordsremember.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;

import com.matteoveroni.wordsremember.R;

/**
 * @author Matteo Veroni
 */
public class GraphicsUtil {

    public static final int getDisplayWidthPx(Display display) {
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static final int getDisplayHeigthPx(Display display) {
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

//    /**
//     * Method that returns orientation (either ORIENTATION_LANDSCAPE, ORIENTATION_PORTRAIT)
//     *
//     * @param context
//     * @return Orientation Landscape int
//     */
//    public static final int getOrientation(Context context) {
//        return context.getResources().getConfiguration().orientation;
//    }
//
//    public static boolean isTablet(Context context) {
//        return (context.getResources().getConfiguration().screenLayout
//                & Configuration.SCREENLAYOUT_SIZE_MASK)
//                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
//    }


}
