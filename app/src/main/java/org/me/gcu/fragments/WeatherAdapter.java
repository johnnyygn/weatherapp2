package org.me.gcu.fragments;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.me.gcu.R;
import org.me.gcu.Weather;

public class WeatherAdapter extends RecyclerView.Adapter<FutureWeatherTab> {

    private final Weather weather;

    public WeatherAdapter(Weather weather) {
        this.weather = weather;
    }

    @NonNull
    @Override
    public FutureWeatherTab onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.future, viewGroup, false);
        return new FutureWeatherTab(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureWeatherTab customViewHolder, int i) {
        try {
            Context context = customViewHolder.itemView.getContext();
            // if there are data then show them to the appropriate field else hide them
            if (weather.getTemperature() != null) {
                customViewHolder.date.setText(weather.getDate());
                customViewHolder.temp.setText(weather.getTemperature());
                if (weather.getPolution().equals("")) {
                    customViewHolder.polution.setVisibility(View.GONE);
                } else {
                    customViewHolder.polution.setText(weather.getPolution());
                }
                if (weather.getWind().equals("")) {
                    customViewHolder.wind.setVisibility(View.GONE);
                } else {
                    customViewHolder.wind.setText(weather.getWind());
                }
                if (weather.getUv().equals("")) {
                    customViewHolder.uv.setVisibility(View.GONE);
                } else {
                    customViewHolder.uv.setText(weather.getUv());
                }
                if (weather.getPressure().equals("")) {
                    customViewHolder.pressure.setVisibility(View.GONE);
                } else {
                    customViewHolder.pressure.setText(weather.getPressure());
                }
                if (weather.getHumidity().equals("")) {
                    customViewHolder.humidity.setVisibility(View.GONE);
                } else {
                    customViewHolder.humidity.setText(weather.getHumidity());
                }
                if (weather.getSunrise() == null || weather.getSunrise().equals("")) {
                    customViewHolder.sunrise.setVisibility(View.GONE);
                } else {
                    customViewHolder.sunrise.setText(weather.getSunrise());
                }
                if (weather.getSunset().equals("")) {
                    customViewHolder.sunset.setVisibility(View.GONE);
                } else {
                    customViewHolder.sunset.setText(weather.getSunset());
                }
                if (weather.getIcon().contains("rain")) {
                    customViewHolder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.outline_umbrella_white_48));
                } else if (weather.getIcon().contains("cloud")) {
                    customViewHolder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.outline_cloud_white_48));
                } else {
                    customViewHolder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.outline_wb_sunny_white_48));
                }
            }
        } catch (Exception ignore) {
        }
    }

    @Override
    public int getItemCount() {
        return weather.getCity() != null ? 1 : 0;
    }
}
