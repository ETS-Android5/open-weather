package com.example.openweather;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WeatherDaily implements Serializable {

   private final long id;
   private final boolean isImperial;
   private final long dt;
   private final long timezoneOffset;
   private final double day;
   private final double min;
   private final double max;
   private final double night;
   private final double eve;
   private final double morn;
   private final String description;
   private final String icon;
   private final double pop;
   private final double uvi;

   public WeatherDaily(long id, boolean isImperial, long dt, long timezoneOffset, double day, double min,
                double max, double night, double eve, double morn, String description, String icon,
                double pop, double uvi) {

      this.id = id;
      this.isImperial = isImperial;
      this.dt = dt;
      this.timezoneOffset = timezoneOffset;
      this.day = day;
      this.min = min;
      this.max = max;
      this.night = night;
      this.eve = eve;
      this.morn = morn;
      this.description = description;
      this.icon = icon;
      this.pop = pop;
      this.uvi = uvi;
   }

   long getId() { return id; }
   String getDay() { return formatTemperature(day, isImperial); }
   String getMin() { return formatTemperature(min, isImperial); }
   String getMax() { return formatTemperature(max, isImperial); }
   String getNight() { return formatTemperature(night, isImperial); }
   String getEve() { return formatTemperature(eve, isImperial); }
   String getMorn() { return formatTemperature(morn, isImperial); }
   String getUvi() { return String.format(Locale.getDefault(), "%.0f", uvi); }
   String getPop() { return String.format(Locale.getDefault(), "%.0f", pop); }
   String getIcon() { return "_" + icon; }
   String getDescription() { return capitalizeFirstLetter(description); }

   @RequiresApi(api = Build.VERSION_CODES.O)
   String getDatetime() {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, MM/dd", Locale.getDefault());
      return dtf.format(LocalDateTime.ofEpochSecond(dt + timezoneOffset, 0, ZoneOffset.UTC));
   }

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
