package com.example.openweather;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherHourlyAdapter extends RecyclerView.Adapter<WeatherHourlyViewHolder> {

    private final MainActivity mainActivity;
    private final ArrayList<WeatherHourly> hourly;

    public WeatherHourlyAdapter(MainActivity mainActivity, ArrayList<WeatherHourly> hourly) {
        this.mainActivity = mainActivity;
        this.hourly = hourly;
    }

    @NonNull
    @Override
    public WeatherHourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_weather_item, parent, false);

        itemView.setOnClickListener(mainActivity);

        return new WeatherHourlyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull WeatherHourlyViewHolder holder, int position) {
        WeatherHourly hourlyItem = hourly.get(position);
        String day = hourlyItem.getDayName();
        String time = hourlyItem.getTime();
        String temp = hourlyItem.getTemp();
        String description = hourlyItem.getDescription().replace(" ", "\n");

        String icon = hourlyItem.getIcon();
        int iconResId = mainActivity.getResources().getIdentifier(icon, "drawable", mainActivity.getPackageName());

        holder.dayView.setText(day);
        holder.timeView.setText(time);
        holder.tempView.setText(temp);
        holder.descriptionView.setText(description);
        holder.iconView.setImageResource(iconResId);
    }

    @Override
    public int getItemCount() { return hourly.size(); }
}
