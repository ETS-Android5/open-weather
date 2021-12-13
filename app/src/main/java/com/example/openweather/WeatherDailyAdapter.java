package com.example.openweather;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherDailyAdapter extends RecyclerView.Adapter<WeatherDailyViewHolder> {

    private final DailyWeatherActivity dailyWeatherActivity;
    private final ArrayList<WeatherDaily> daily;
    public WeatherDailyAdapter(DailyWeatherActivity dailyWeatherActivity, ArrayList<WeatherDaily> daily) {
        this.dailyWeatherActivity = dailyWeatherActivity;
        this.daily = daily;
    }

    @NonNull
    @Override
    public WeatherDailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_weather_item, parent, false);
        return new WeatherDailyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull WeatherDailyViewHolder holder, int position) {
        WeatherDaily dailyItem = daily.get(position);
        String datetime = dailyItem.getDatetime();
        String min = dailyItem.getMin();
        String max = dailyItem.getMax();
        String morn = dailyItem.getMorn();
        String day = dailyItem.getDay();
        String eve = dailyItem.getEve();
        String night = dailyItem.getNight();
        String pop = dailyItem.getPop();
        String uvi = dailyItem.getUvi();
        String description = dailyItem.getDescription();

        String icon = dailyItem.getIcon();
        int iconResId = dailyWeatherActivity.getResources().getIdentifier(icon, "drawable", dailyWeatherActivity.getPackageName());

        String minMaxText = min + "/" + max;
        String popText = "(" + pop + "% precip.)";
        String uviText = "UV Index: " + uvi;

        holder.datetimeView.setText(datetime);
        holder.minMaxTempView.setText(minMaxText);
        holder.descriptionView.setText(description);
        holder.popView.setText(popText);
        holder.uviView.setText(uviText);
        holder.mornTempView.setText(morn);
        holder.dayTempView.setText(day);
        holder.eveTempView.setText(eve);
        holder.nightTempView.setText(night);
        holder.mornTimeView.setText(R.string._8am);
        holder.dayTimeView.setText(R.string._1pm);
        holder.eveTimeView.setText(R.string._5pm);
        holder.nightTimeView.setText(R.string._11pm);
        holder.iconView.setImageResource(iconResId);
    }

    @Override
    public int getItemCount() { return daily.size(); }
}
