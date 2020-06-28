package com.example.android.weatherapp2.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.weatherapp2.Common.Common;
import com.example.android.weatherapp2.Common.Model.WeatherForecastResult;
import com.example.android.weatherapp2.R;
import com.squareup.picasso.Picasso;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

    Context context;

    WeatherForecastResult weatherForecastResult;

    public ForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView= LayoutInflater.from(context).inflate(R.layout.weather_card,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
// Load icon
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/").append(weatherForecastResult.list.get(i).weather.get(0).getIcon())
                .append(".png").toString()).into(myViewHolder.img_weather);

        myViewHolder.date.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResult.list.get(i).dt)));

        myViewHolder.city_description.setText( new StringBuilder(weatherForecastResult.list.get(i).weather.get(0).getDescription()));

        myViewHolder.city_temperature.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(i).main.getTemp())).append("°C"));
    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date,city_description,city_temperature;
        ImageView img_weather;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_weather=(ImageView)itemView.findViewById(R.id.img_weather);
            date=(TextView)itemView.findViewById(R.id.date);
            city_description=(TextView)itemView.findViewById(R.id.city_description);
            city_temperature=(TextView)itemView.findViewById(R.id.city_temperature);


        }
    }
}
