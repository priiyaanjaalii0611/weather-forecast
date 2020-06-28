package com.example.android.weatherapp2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.weatherapp2.Adapter.ForecastAdapter;
import com.example.android.weatherapp2.Common.Common;
import com.example.android.weatherapp2.Common.Model.WeatherForecastResult;
import com.example.android.weatherapp2.Retrofit.IOpenWeatherMap;
import com.example.android.weatherapp2.Retrofit.RetrofitClient;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {

    CompositeDisposable compositeDisposable;

    IOpenWeatherMap mService;

    TextView city_name, geo_coord_value;
    RecyclerView recycler_forecast;

  static ForecastFragment instance;

   public static ForecastFragment getInstance(){
       if(instance==null)

           instance= new ForecastFragment();
       return instance;
   }

    public ForecastFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit= RetrofitClient.getInstance();
        mService=retrofit.create(IOpenWeatherMap.class);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_forecast, container, false);

        city_name=(TextView)itemView.findViewById(R.id.city_name);
        geo_coord_value=(TextView)itemView.findViewById(R.id.geo_coords_value);

        recycler_forecast=(RecyclerView)itemView.findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        getForecastWeatherInformation();
        return itemView;
    }

    private void getForecastWeatherInformation() {
       compositeDisposable.add(mService.getForeCastWeatherByLatLon(String.valueOf(Common.current_location.getLatitude()),
               String.valueOf(Common.current_location.getLongitude()),Common.API_ID,
               "metric"      ).subscribeOn(Schedulers.io())
       .observeOn(AndroidSchedulers.mainThread())
       .subscribe(new Consumer<WeatherForecastResult>() {
           @Override
           public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
               displayForecastWeather(weatherForecastResult);

           }
       }, new Consumer<Throwable>() {
           @Override
           public void accept(Throwable throwable) throws Exception {
               Log.d("Error",""+throwable.getMessage());
           }
       })
       );
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
       city_name.setText(new StringBuilder(weatherForecastResult.city.name));
       geo_coord_value.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));

        ForecastAdapter adapter=new ForecastAdapter(getContext(),weatherForecastResult);
        recycler_forecast.setAdapter(adapter);
    }
}