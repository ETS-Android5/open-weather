package com.example.openweather;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class WeatherDownloadRunnable implements Runnable {

    private final MainActivity mainActivity;

    private final String lat;
    private final String lon;
    private final boolean isImperial;
    private final String lang = "en";
    private final String exclude = "minutely";

    private final String weatherURL = "https://api.openweathermap.org/data/2.5/onecall";
    private static final String apiKey = "1fadc52ff05b11487cdbfafe9e4db6a3";

    WeatherDownloadRunnable(MainActivity mainActivity, String lat, String lon, boolean isImperial) {
        this.mainActivity = mainActivity;
        this.lat = lat;
        this.lon = lon;
        this.isImperial = isImperial;
    }

    WeatherDownloadRunnable(MainActivity mainActivity, String lat, String lon) {
        this.mainActivity = mainActivity;
        this.lat = lat;
        this.lon = lon;
        this.isImperial = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();

        buildURL.appendQueryParameter("lat", lat);
        buildURL.appendQueryParameter("lon", lon);
        buildURL.appendQueryParameter("units", isImperial ? "imperial" : "metric");
        buildURL.appendQueryParameter("lang", lang);
        buildURL.appendQueryParameter("exclude", exclude);
        buildURL.appendQueryParameter("appid", apiKey);

        String urlToUse = buildURL.build().toString();

        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(urlToUse);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                handleResults(null);
                return;
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleResults(null);
            return;
        }
        handleResults(sb.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleResults(final String jsonString) {
        final WeatherLocation w = parseJSONWeatherLocation(jsonString);
        mainActivity.runOnUiThread(() -> mainActivity.updateData(w));
    }

    private WeatherLocation parseJSONWeatherLocation(String s) {
        try {
            JSONObject jsonObjMain = new JSONObject(s);
            double lat = jsonObjMain.getDouble("lat");
            double lon = jsonObjMain.getDouble("lon");
            String timezone = jsonObjMain.getString("timezone");
            long timezoneOffset = jsonObjMain.getLong("timezone_offset");

            WeatherCurrent current = parseJsonCurrent(jsonObjMain.getJSONObject("current"), timezoneOffset);
            ArrayList<WeatherHourly> hourly = new ArrayList<>();
            ArrayList<WeatherDaily> daily = new ArrayList<>();

            JSONArray jsonHourlyArray = jsonObjMain.getJSONArray("hourly");
            JSONArray jsonDailyArray = jsonObjMain.getJSONArray("daily");

            for (int i = 0; i < jsonHourlyArray.length(); i++)
                hourly.add(parseJsonHourly((JSONObject) jsonHourlyArray.get(i), timezoneOffset));

            for (int i = 0; i < jsonDailyArray.length(); i++)
                daily.add(parseJsonDaily((JSONObject) jsonDailyArray.get(i), timezoneOffset));

            return new WeatherLocation(lat, lon, timezone, timezoneOffset, current, hourly, daily);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private WeatherCurrent parseJsonCurrent(JSONObject jsonObject, long timezoneOffset) {
        try {
            JSONObject jWeather = (JSONObject) jsonObject.getJSONArray("weather").get(0);

            long id = jWeather.getLong("id");
            String main = jWeather.getString("main");
            String description = jWeather.getString("description");
            String icon = jWeather.getString("icon");

            long dt = jsonObject.getLong("dt");
            long sunrise = jsonObject.getLong("sunrise");
            long sunset = jsonObject.getLong("sunset");
            double temp = jsonObject.getDouble("temp");
            double feelsLike = jsonObject.getDouble("feels_like");
            double pressure = jsonObject.getDouble("pressure");
            double humidity = jsonObject.getDouble("humidity");
            double uvi = jsonObject.getDouble("uvi");
            double visibility = jsonObject.getDouble("visibility");
            String clouds = jsonObject.getDouble("clouds") + "%";
            double windSpeed = jsonObject.getDouble("wind_speed");
            double windDeg = jsonObject.getDouble("wind_deg");
            double windGust = 0;
            String rain = "";
            String snow = "";

            if (jsonObject.has("wind_gust"))
                windGust = jsonObject.getDouble("wind_gust");

            if (jsonObject.has("rain"))
                try {
                    rain = jsonObject.getDouble("rain") + " mm";
                } catch (Exception e) {
                    rain = jsonObject.getJSONObject("rain").getDouble("1h") + " mm";
                }

            if (jsonObject.has("snow"))
                try {
                    snow = jsonObject.getDouble("snow") + " mm";
                } catch (Exception e) {
                    snow = jsonObject.getJSONObject("snow").getDouble("1h") + " mm";
                }

            return new WeatherCurrent(id, isImperial, timezoneOffset, main, description, icon, dt,
                    sunrise, sunset, temp, feelsLike, pressure, humidity, visibility,  uvi, clouds,
                    windSpeed, windDeg, windGust, rain, snow);

            } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private WeatherHourly parseJsonHourly(JSONObject jsonObject, long timezoneOffset) {
        try {
            JSONObject jWeather = (JSONObject) jsonObject.getJSONArray("weather").get(0);

            String description = jWeather.getString("description");
            String icon = jWeather.getString("icon");

            long dt = jsonObject.getLong("dt");
            double temp = jsonObject.getDouble("temp");
            double pop = jsonObject.getDouble("pop");

            return new WeatherHourly(dt, isImperial, timezoneOffset, temp, description, icon, pop);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private WeatherDaily parseJsonDaily(JSONObject jsonObject, long timezoneOffset) {
        try {
            JSONObject jWeather = (JSONObject) jsonObject.getJSONArray("weather").get(0);
            JSONObject jTemp =  jsonObject.getJSONObject("temp");

            long id = jWeather.getLong("id");
            String description = jWeather.getString("description");
            String icon = jWeather.getString("icon");

            long dt = jsonObject.getLong("dt");
            double pop = jsonObject.getDouble("pop");
            double uvi = jsonObject.getDouble("uvi");

            double day = jTemp.getDouble("day");
            double min = jTemp.getDouble("min");
            double max = jTemp.getDouble("max");
            double night = jTemp.getDouble("night");
            double eve = jTemp.getDouble("eve");
            double morn = jTemp.getDouble("morn");

            return new WeatherDaily(id, isImperial, dt, timezoneOffset, day, min, max, night, eve, morn, description,
                    icon, pop, uvi);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
