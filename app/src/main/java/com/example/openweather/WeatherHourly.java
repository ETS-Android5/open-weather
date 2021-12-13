package com.example.openweather;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WeatherHourly {

    private final long dt;
    private final boolean isImperial;
    private final long timezoneOffset;
    private final double temp;
    private final String description;
    private final String icon;
    private final double pop;

    private final String dayNameFormat = "EEEE";
    private final String timeFormat = "h:mm a";

    public WeatherHourly(long dt, boolean isImperial, long timezoneOffset, double temp, String description, String icon, double pop) {
        this.dt = dt;
        this.isImperial = isImperial;
        this.timezoneOffset = timezoneOffset;
        this.temp = temp;
        this.description = description;
        this.icon = icon;
        this.pop = pop;
    }

    long getDt() { return dt; }

    long getTimezoneOffset() { return timezoneOffset; }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String getDayName() {
        DateTimeFormatter dnf = DateTimeFormatter.ofPattern(dayNameFormat, Locale.getDefault());
        String dayName = dnf.format(LocalDateTime.ofEpochSecond(dt + timezoneOffset, 0, ZoneOffset.UTC));
        String todayName = dnf.format(LocalDateTime.now());
        return dayName.equals(todayName) ? "Today" : dayName;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String getTime() {
        DateTimeFormatter tf = DateTimeFormatter.ofPattern(timeFormat, Locale.getDefault());
        return tf.format(LocalDateTime.ofEpochSecond(dt + timezoneOffset, 0, ZoneOffset.UTC));
    }

    String getTemp() { return formatTemperature(temp, isImperial); }

    String getDescription() { return capitalizeFirstLetter(description); }

    String getIcon() { return "_" + icon; }

    double getPop() { return pop; }

    private String formatTemperature(double temp, boolean isImperial) {
        String tempFormat = "%d°%s";
        return String.format(Locale.getDefault(), tempFormat, (int) temp, isImperial ? "F" : "C");
    }

    private String capitalizeFirstLetter(String s) {
        String[] words = s.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            String first = word.substring(0, 1);
            String rest = word.substring(1);
            result.append(first.toUpperCase()).append(rest).append(" ");
        }
        return result.toString().trim();
    }
}
