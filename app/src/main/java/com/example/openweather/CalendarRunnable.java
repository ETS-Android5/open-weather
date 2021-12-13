package com.example.openweather;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;

public class CalendarRunnable implements Runnable {

    private final MainActivity mainActivity;
    private final long epochTimeMilli;

    CalendarRunnable(MainActivity mainActivity, long epochTimeMilli) {
        this.mainActivity = mainActivity;
        this.epochTimeMilli = epochTimeMilli;
    }

    @Override
    public void run() {
        Uri.Builder calendarBuilder = CalendarContract.CONTENT_URI.buildUpon().appendPath("time");
        ContentUris.appendId(calendarBuilder, epochTimeMilli);
        Uri calendarUri = calendarBuilder.build();

        Intent calendarIntent = new Intent(Intent.ACTION_VIEW).setData(calendarUri);
        mainActivity.runOnUiThread(() -> mainActivity.startActivity(calendarIntent));
    }
}
