package com.example.openweather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherDailyViewHolder extends RecyclerView.ViewHolder {

    TextView datetimeView;
    TextView minMaxTempView;
    TextView descriptionView;
    TextView popView;
    TextView uviView;
    ImageView iconView;
    TextView mornTempView;
    TextView dayTempView;
    TextView eveTempView;
    TextView nightTempView;
    TextView mornTimeView;
    TextView dayTimeView;
    TextView eveTimeView;
    TextView nightTimeView;

    public WeatherDailyViewHolder(@NonNull View itemView) {
        super(itemView);
        datetimeView = itemView.findViewById(R.id.daily_datetime);
        minMaxTempView = itemView.findViewById(R.id.daily_minmax_temp);
        iconView = itemView.findViewById(R.id.daily_icon);
        descriptionView = itemView.findViewById(R.id.daily_description);
        popView = itemView.findViewById(R.id.daily_pop);
        uviView = itemView.findViewById(R.id.daily_uvi);
        mornTempView = itemView.findViewById(R.id.daily_morning_temp);
        dayTempView = itemView.findViewById(R.id.daily_day_temp);
        eveTempView = itemView.findViewById(R.id.daily_evening_temp);
        nightTempView = itemView.findViewById(R.id.daily_night_temp);
        mornTimeView = itemView.findViewById(R.id.daily_morning_time);
        dayTimeView = itemView.findViewById(R.id.daily_day_time);
        eveTimeView = itemView.findViewById(R.id.daily_evening_time);
        nightTimeView = itemView.findViewById(R.id.daily_night_time);
    }
}