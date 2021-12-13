package com.example.openweather;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherLocation implements Serializable {

    private double lat;
    private double lon;
    private String timezone;
    private long timezoneOffset;
    private WeatherCurrent current;
    private ArrayList<WeatherHourly> hourly;
    private ArrayList<WeatherDaily> daily;

    public WeatherLocation(double lat, double lon, String timezone, long timezoneOffset, WeatherCurrent current,
                           ArrayList<WeatherHourly> hourly, ArrayList<WeatherDaily> daily) {

        this.lat = lat;
        this.lon = lon;
        this.timezone = timezone;
        this.timezoneOffset = timezoneOffset;
        this.current = current;
        this.hourly = hourly;
        this.daily = daily;
    }

    double getLat() { return lat; }
    double getLon() { return lon; }
    String getTimezone() { return timezone; }
    long getTimezoneOffset() { return timezoneOffset; }
    WeatherCurrent getCurrent() { return current; }
    ArrayList<WeatherHourly> getHourly() { return hourly; }
    ArrayList<WeatherDaily> getDaily() { return daily; }
}