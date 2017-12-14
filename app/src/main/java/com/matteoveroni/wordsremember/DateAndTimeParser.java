package com.matteoveroni.wordsremember;

import android.content.Context;

import com.matteoveroni.androidtaggenerator.TagGenerator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Matteo Veroni
 */

public class DateAndTimeParser {
    private final DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Date parseStringToDate(String dateTime) throws ParseException {
        return iso8601Format.parse(dateTime);
    }

    public String getStringDateAndTimeForAndroid(Context context, Date date) {
        long when = date.getTime();
        int flags = 0;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
        flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

        String finalDateTime = android.text.format.DateUtils.formatDateTime(
                context, when + TimeZone.getDefault().getOffset(when), flags
        );
        return finalDateTime;
    }
}
