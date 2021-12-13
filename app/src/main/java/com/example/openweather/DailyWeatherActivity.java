package com.example.openweather;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DailyWeatherActivity extends AppCompatActivity {

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);

        ArrayList<WeatherDaily> daily = new ArrayList<WeatherDaily>();

        Intent intent = getIntent();

        if (intent.hasExtra("DAILY")) {
            daily = (ArrayList<WeatherDaily>) intent.getSerializableExtra("DAILY");
        }

        if (intent.hasExtra("LOCATION")) {
            String location = intent.getStringExtra("LOCATION");
            setTitle(location);
        }

        RecyclerView weatherDailyRecyclerView = findViewById(R.id.daily_recycler);
        WeatherDailyAdapter weatherDailyAdapter = new WeatherDailyAdapter(this, daily);

        weatherDailyRecyclerView.setAdapter(weatherDailyAdapter);
        weatherDailyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}