package com.example.openweather;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WeatherCurrent implements Serializable {

    private final long id;
    private final boolean isImperial;
    private final String main;
    private final String description;
    private final String icon;
    private final long dt;
    private final long sunrise;
    private final long sunset;
    private final double temp;
    private final double feelsLike;
    private final double pressure;
    private final double humidity;
    private final double uvi;
    private final double visibility;
    private final String clouds;
    private final double windSpeed;
    private final double windDeg;
    private final double windGust;
    private final String rain;
    private final String snow;
    private final long timezoneOffset;

    private final String dateTimeFormat = "EEE MMM dd h:mm a, yyyy";
    private final String timeFormat = "h:mm a";

    WeatherCurrent(long id, boolean isImperial, long timezoneOffset, String main, String description,
                   String icon, long dt, long sunrise, long sunset, double temp, double feelsLike,
                   double pressure, double humidity, double visibility, double uvi, String clouds,
                   double windSpeed, double windDeg, double windGust, String rain, String snow) {

        this.id = id;
        this.isImperial = isImperial;
        this.timezoneOffset = timezoneOffset;
        this.main = main;
        this.description = description;
        this.icon = icon;
        this.dt = dt;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.pressure = pressure;
        this.humidity = humidity;
        this.visibility = visibility;
        this.uvi = uvi;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;
        this.windGust = windGust;
        this.rain = rain;
        this.snow = snow;
    }

    private String formatTemperature(double temp, boolean isImperial) {
        String tempFormat = "%d°%s";
        return String.format(Locale.getDefault(), tempFormat, (int) temp, isImperial ? "F" : "C");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String formatDateTime(long dt, long timezoneOffset, String format) {
        LocalDateTime ldt =
                LocalDateTime.ofEpochSecond(dt + timezoneOffset, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern(format, Locale.getDefault());
        return ldt.format(dtf);
    }

    String getTemp() {
        return formatTemperature(temp, isImperial);
    }

    String getFeelsLike() {
        return formatTemperature(feelsLike, isImperial);
    }

    String getHumidity() { return String.format(Locale.getDefault(), "%.0f", humidity) + "%"; }

    String getUvi() { return String.format(Locale.getDefault(), "%.0f", uvi); }

    String getVisibility() {
        double distanceInch = meterToInch(visibility);
        return isImperial ? String.format(Locale.getDefault(), "%.1f", distanceInch) + " mi" : visibility + " m";
    }

    String getDescription() {
        String extraDescription = "";
        switch (main.toLowerCase()) {
            case "clouds":
                if (!(clouds == null || clouds.isEmpty()))
                    extraDescription += " (" + clouds + " clouds)";
                break;
            case "rain":
                if (!(rain == null || rain.isEmpty()))
                    extraDescription += " (" + rain + " rain)";
                break;
            case "snow":
                if (!(snow == null || snow.isEmpty()))
                    extraDescription += " (" + snow + " snow)";
                break;
        }

        if (extraDescription.isEmpty()) extraDescription += " (" + clouds + " clouds)";

        return capitalizeFirstLetter(description) + extraDescription;
    }

    String getIcon() { return "_" + icon; }

    String getWinds() {
        String direction = getWindDirection(windDeg);
        return (direction.equals("X") ? "" : direction + " at ") + windSpeed + (isImperial ? " mph" : " m/s");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String getSunrise() {
        return formatDateTime(sunrise, timezoneOffset, timeFormat);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String getSunset() {
        return formatDateTime(sunset, timezoneOffset, timeFormat);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String getDt() {
        return formatDateTime(dt, timezoneOffset, dateTimeFormat);
    }

    private String getWindDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X";
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

    private double meterToInch(double distance) {
        return distance / 39.37;
    }
}