package com.example.openweather;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private UserPreferences userPreferences;

    private boolean isImperial = true;
    private String location = "Chicago, IL";
    private String lat = "41.85";
    private String lon = "-87.65";

    private final ArrayList<WeatherHourly> hourly = new ArrayList<>();
    private final ArrayList<WeatherDaily> daily = new ArrayList<>();

    private Menu mainMenu;

    private RecyclerView weatherHourlyRecyclerView;
    private WeatherHourlyAdapter weatherHourlyAdapter;

    private TextView locationView;
    private TextView tempView;
    private TextView datetimeView;
    private TextView feelsLikeView;
    private TextView descriptionView;
    private TextView windView;
    private TextView humidityView;
    private TextView uviView;
    private TextView visibilityView;
    private TextView sunriseView;
    private TextView sunsetView;
    private TextView mornTempView;
    private TextView dayTempView;
    private TextView eveTempView;
    private TextView nightTempView;
    private TextView mornTimeView;
    private TextView dayTimeView;
    private TextView eveTimeView;
    private TextView nightTimeView;
    private ImageView iconView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("OpenWeather App");

        userPreferences = new UserPreferences(this);

        hourly.clear();
        daily.clear();

        weatherHourlyRecyclerView = findViewById(R.id.hourly_recycler);
        weatherHourlyAdapter = new WeatherHourlyAdapter(this, hourly);

        weatherHourlyRecyclerView.setAdapter(weatherHourlyAdapter);
        weatherHourlyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        locationView = findViewById(R.id.location);
        datetimeView = findViewById(R.id.datetime);
        tempView = findViewById(R.id.temp);
        feelsLikeView = findViewById(R.id.feels_like);
        descriptionView = findViewById(R.id.description);
        windView = findViewById(R.id.winds);
        humidityView = findViewById(R.id.humidity);
        uviView = findViewById(R.id.uvi);
        visibilityView = findViewById(R.id.visibility);
        sunriseView = findViewById(R.id.sunrise);
        sunsetView = findViewById(R.id.sunset);
        mornTempView = findViewById(R.id.morning_temp);
        dayTempView = findViewById(R.id.day_temp);
        eveTempView = findViewById(R.id.evening_temp);
        nightTempView = findViewById(R.id.night_temp);
        mornTimeView = findViewById(R.id.morning_time);
        dayTimeView = findViewById(R.id.day_time);
        eveTimeView = findViewById(R.id.evening_time);
        nightTimeView = findViewById(R.id.night_time);
        iconView = findViewById(R.id.icon);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserPreferences();
        doDownload();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveUserPreferences();
    }

    public void loadUserPreferences() {
        String userUnit = userPreferences.getValue("TEMPERATURE_UNIT");
        String userLat = userPreferences.getValue("LAT");
        String userLon = userPreferences.getValue("LON");
        String userLocation = userPreferences.getValue("LOCATION");

        isImperial = userUnit.isEmpty() || userUnit.equals("F");

        if (!userLat.isEmpty()) lat = userLat;

        if (!userLon.isEmpty()) lon = userLon;

        if (!userLocation.isEmpty()) location = userLocation;
    }

    public void saveUserPreferences() {
        userPreferences.save("TEMPERATURE_UNIT", isImperial ? "F" : "C");
        userPreferences.save("LAT", lat);
        userPreferences.save("LON", lon);
        userPreferences.save("LOCATION", location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mainMenu = menu;
        updateUnitMenuItemIcon(isImperial);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean hasInternetConnection = hasNetworkConnection();
        if (hasInternetConnection) {
            if (item.getItemId() == R.id.temperature_unit_option) {
                isImperial = !isImperial;
                updateUnitMenuItemIcon(isImperial);
                saveUserPreferences();
                doDownload();
                return true;
            } else if (item.getItemId() == R.id.calendar_option) {
                Intent intent = new Intent(this, DailyWeatherActivity.class);
                intent.putExtra("DAILY", daily);
                intent.putExtra("LOCATION", location);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.location) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("Enter a Location");
                dialogBuilder.setMessage("For US location, enter as 'City', or 'City, State'\n\nFor international locations enter as 'City, Country'");

                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);
                dialogBuilder.setView(et);

                dialogBuilder.setPositiveButton("OK", (dialog, id) -> {
                    String userLocation = et.getText().toString();
                    try {
                        GeoLocationGetLocationRunnable loaderTaskRunnable = new GeoLocationGetLocationRunnable(this, userLocation);
                        new Thread(loaderTaskRunnable).start();
                    } catch (Exception e) {
                        String errorMessage = String.format("Could not find the given location: \"%s\"", et.getText().toString());
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });

                dialogBuilder.setNegativeButton("CANCEL", (dialog, id) -> {
                });

                AlertDialog locationDialog = dialogBuilder.create();
                locationDialog.show();

                return true;
            }
        } else {
            Toast.makeText(this, "Cannot use menu options without internet connection.", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData(WeatherLocation weatherLocation) {
        if (weatherLocation == null) {
            Toast.makeText(this, "Cannot get weather data from the internet. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        WeatherCurrent current = weatherLocation.getCurrent();
        ArrayList<WeatherHourly> hourly = weatherLocation.getHourly();
        ArrayList<WeatherDaily> daily = weatherLocation.getDaily();

        this.hourly.clear();
        this.hourly.addAll(hourly);
        weatherHourlyAdapter.notifyDataSetChanged();

        this.daily.clear();
        this.daily.addAll(daily);

        String feelsLike = "Feels like " + current.getFeelsLike();
        String winds = "Winds: " + current.getWinds();
        String humidity = "Humidity: " + current.getHumidity();
        String uvi = "UV Index: " + current.getUvi();
        String visibility = "Visibility: " + current.getVisibility();
        String sunrise = "Sunrise: " + current.getSunrise();
        String sunset = "Sunset: " + current.getSunset();
        String morn = daily.get(0).getMorn();
        String day = daily.get(0).getDay();
        String eve = daily.get(0).getEve();
        String night = daily.get(0).getNight();

        String icon = current.getIcon();
        int iconResId = this.getResources().getIdentifier(icon, "drawable", this.getPackageName());

        locationView.setText(location);
        datetimeView.setText(current.getDt());
        tempView.setText(current.getTemp());
        feelsLikeView.setText(feelsLike);
        descriptionView.setText(current.getDescription());
        windView.setText(winds);
        humidityView.setText(humidity);
        uviView.setText(uvi);
        visibilityView.setText(visibility);
        sunriseView.setText(sunrise);
        sunsetView.setText(sunset);
        mornTempView.setText(morn);
        dayTempView.setText(day);
        eveTempView.setText(eve);
        nightTempView.setText(night);
        mornTimeView.setText(R.string._8am);
        dayTimeView.setText(R.string._1pm);
        eveTimeView.setText(R.string._5pm);
        nightTimeView.setText(R.string._11pm);
        iconView.setImageResource(iconResId);
    }

    public void handleLocationResult(String location) {
        if (location != null && !location.isEmpty()) {
            this.location = location;
            GeoLocationGetLatLonRunnable loaderTaskRunnable = new GeoLocationGetLatLonRunnable(this, location);
            new Thread(loaderTaskRunnable).start();
        } else {
            Toast.makeText(this, "Could not find the given location.", Toast.LENGTH_LONG).show();
        }
    }

    public void handleLatLonResult(double[] latLon) {
        if (latLon != null) {
            lat = String.valueOf(latLon[0]);
            lon = String.valueOf(latLon[1]);
            saveUserPreferences();
            doDownload();
        } else {
            Toast.makeText(this, "Could not find the given location.", Toast.LENGTH_LONG).show();
        }
    }

    public void doDownload() {
        boolean hasInternetConnection = hasNetworkConnection();
        if (hasInternetConnection) {
            WeatherDownloadRunnable loaderTaskRunnable = new WeatherDownloadRunnable(this, lat, lon, isImperial);
            new Thread(loaderTaskRunnable).start();
        } else {
            clearFields();
            datetimeView.setText(R.string.no_internet_connection);
            Toast.makeText(this, "Download failed. No internet connection.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        int position = weatherHourlyRecyclerView.getChildLayoutPosition(view);
        WeatherHourly hourlyItem = hourly.get(position);
        long hourlyDatetime = hourlyItem.getDt();
        long hourlyTimezoneOffset = hourlyItem.getTimezoneOffset();
        long hourlyEpochMilli = secondToMillisecond(hourlyDatetime + hourlyTimezoneOffset);

        CalendarRunnable loaderTaskRunnable = new CalendarRunnable(this, hourlyEpochMilli);
        new Thread(loaderTaskRunnable).start();
    }

    private long secondToMillisecond(long second) {
        return second * 1000;
    }

    private void updateUnitMenuItemIcon(boolean isImperial) {
        int temperatureUnitDrawable = isImperial ? R.drawable.units_f : R.drawable.units_c;
        mainMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, temperatureUnitDrawable));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void clearFields() {
        daily.clear();
        hourly.clear();
        weatherHourlyAdapter.notifyDataSetChanged();

        locationView.setText("");
        datetimeView.setText("");
        tempView.setText("");
        feelsLikeView.setText("");
        descriptionView.setText("");
        windView.setText("");
        humidityView.setText("");
        uviView.setText("");
        visibilityView.setText("");
        sunriseView.setText("");
        sunsetView.setText("");
        mornTempView.setText("");
        dayTempView.setText("");
        eveTempView.setText("");
        nightTempView.setText("");
        mornTimeView.setText("");
        dayTimeView.setText("");
        eveTimeView.setText("");
        nightTimeView.setText("");
        iconView.setImageResource(0);
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}