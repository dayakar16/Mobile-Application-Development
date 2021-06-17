package com.ahari.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*
    HW05
    MainActivity
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class MainActivity extends AppCompatActivity implements CitiesFragment.ICitiesFragmentListener, CurrentWeatherFragment.ICurrentWeatherForecastListener {

    private String CITIES_FRAGMENT = "CITIES_FRAGMENT";
    private String CURRENT_WEATHER_FRAGMENT = "CURRENT_WEATHER_FRAGMENT";
    private String WEATHER_FORECAST_FRAGMENT = "WEATHER_FORECAST_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, CitiesFragment.newInstance(), CITIES_FRAGMENT)
                .commit();
    }

    @Override
    public void callForCurrentWeather(Data.City cityName) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, CurrentWeatherFragment.newInstance(cityName), CURRENT_WEATHER_FRAGMENT)
                .addToBackStack(CITIES_FRAGMENT)
                .commit();
    }

    @Override
    public void callWeatherForecast(Data.City cityName) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, WeatherForecastFragment.newInstance(cityName), WEATHER_FORECAST_FRAGMENT)
                .addToBackStack(CURRENT_WEATHER_FRAGMENT)
                .commit();
    }
}