package com.example.openweather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherHourlyViewHolder extends RecyclerView.ViewHolder {
    TextView dayView;
    TextView timeView;
    ImageView iconView;
    TextView tempView;
    TextView descriptionView;

    public WeatherHourlyViewHolder(@NonNull View itemView) {
        super(itemView);
        dayView = itemView.findViewById(R.id.hourly_day);
        timeView = itemView.findViewById(R.id.hourly_time);
        iconView = itemView.findViewById(R.id.hourly_icon);
        tempView = itemView.findViewById(R.id.hourly_temp);
        descriptionView = itemView.findViewById(R.id.hourly_description);
    }
}
