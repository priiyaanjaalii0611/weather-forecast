package com.example.android.weatherapp2;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.weatherapp2.Common.Common;
import com.example.android.weatherapp2.Common.Model.WeatherRes;
import com.example.android.weatherapp2.Retrofit.IOpenWeatherMap;
import com.example.android.weatherapp2.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayWeatherFragment#} factory method to
 * create an instance of this fragment.
 */
public class TodayWeatherFragment extends Fragment {


     ImageView img_weather;
     TextView city_name,sunrise_value,humidity_value,pressure_value,sunset_value,city_temperature,city_description,date,wind_value,geo_coords_value;
     LinearLayout weather_panel;
     ProgressBar loading;


     CompositeDisposable compositeDisposable;

     IOpenWeatherMap mService;


  static  TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance() {
        if(instance==null)
            instance= new TodayWeatherFragment();
        return instance;
    }

    public TodayWeatherFragment() {
     compositeDisposable= new CompositeDisposable();
        Retrofit retrofit= RetrofitClient.getInstance();
        mService= retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View itemView= inflater.inflate(R.layout.fragment_today_weather, container, false);
      img_weather=(ImageView)itemView.findViewById(R.id.img_weather);
      city_name= (TextView)itemView.findViewById(R.id.city_name);
        city_description= (TextView)itemView.findViewById(R.id.city_description);
        date= (TextView)itemView.findViewById(R.id.date);
        humidity_value= (TextView)itemView.findViewById(R.id.humidity_value);
        sunrise_value= (TextView)itemView.findViewById(R.id.sunrise_value);
        sunset_value= (TextView)itemView.findViewById(R.id.sunset_value);
        pressure_value=(TextView)itemView.findViewById(R.id.pressure_value);
        geo_coords_value=(TextView)itemView.findViewById(R.id.geo_coords_value);
        wind_value=(TextView)itemView.findViewById(R.id.wind_value);
        city_temperature=(TextView)itemView.findViewById(R.id.city_temperature);


        weather_panel=(LinearLayout)itemView.findViewById(R.id.weather_panel);
        loading=(ProgressBar) itemView.findViewById(R.id.loading);

        getWeatherInfo();

      return itemView;
    }

    private void getWeatherInfo() {
        compositeDisposable.add(mService.getWeatherByLatLon(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.API_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherRes>() {
                    @Override
                    public void accept(WeatherRes weatherRes) throws Exception {
                        //Load image
                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/").append(weatherRes.getWeather().get(0)
                        .getIcon()).append(".png").toString()).into(img_weather);

                        //Load information

                        city_name.setText(weatherRes.getName());
                        city_description.setText(new StringBuilder("Weather in").append(weatherRes.getName()).toString());
                        city_temperature.setText(new StringBuilder(String.valueOf(weatherRes.getMain().getTemp())).append("°C").toString());
                        date.setText(Common.convertUnixToDate(weatherRes.getDt()));
                        pressure_value.setText(new StringBuilder(String.valueOf(weatherRes.getMain().getPressure())).append(" hpa").toString());
                        humidity_value.setText(new StringBuilder(String.valueOf(weatherRes.getMain().getHumidity())).append(" %").toString());
                        sunrise_value.setText(Common.convertUnixToHour(weatherRes.getSys().getSunrise()));
                        sunset_value.setText(Common.convertUnixToHour(weatherRes.getSys().getSunset()));
                        geo_coords_value.setText(new StringBuilder("[").append(weatherRes.getCoord().toString()).append("]").toString());

                        //display panel

                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(),""+throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }

    })

        );
    }
}