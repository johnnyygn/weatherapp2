package org.me.gcu.fragments;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.me.gcu.R;

public class FutureWeatherTab extends RecyclerView.ViewHolder {
    public TextView humidity, temp, polution, date, wind, pressure, uv, sunset, sunrise;
    public ImageView icon;

    public FutureWeatherTab(View view) {
        super(view);
        // initialize the views of the fields
        this.date = view.findViewById(R.id.date);
        this.temp = view.findViewById(R.id.temp);
        this.icon = view.findViewById(R.id.icon);
        this.polution = view.findViewById(R.id.polution);
        this.wind = view.findViewById(R.id.wind);
        this.pressure = view.findViewById(R.id.pressure);
        this.humidity = view.findViewById(R.id.humidity);
        this.uv = view.findViewById(R.id.uv);
        this.sunset = view.findViewById(R.id.sunset);
        this.sunrise = view.findViewById(R.id.sunrise);
    }
}
