package com.example.android.weatherapp2.Retrofit;

import com.example.android.weatherapp2.Common.Model.WeatherForecastResult;
import com.example.android.weatherapp2.Common.Model.WeatherRes;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherRes> getWeatherByLatLon(@Query("lat") String lat,
                                              @Query("lon") String lon,
                                              @Query("appid") String appid,
                                              @Query("units") String unit);
    @GET("forecast")
    Observable<WeatherForecastResult> getForeCastWeatherByLatLon(@Query("lat") String lat,
                                                                 @Query("lon") String lon,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit);
}
