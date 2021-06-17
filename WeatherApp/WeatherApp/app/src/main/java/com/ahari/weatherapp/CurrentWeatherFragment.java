package com.ahari.weatherapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahari.weatherapp.pojos.WeatherResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
    HW05
    CurrentWeatherFragment
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class CurrentWeatherFragment extends Fragment {

    private static final String CITY_NAME = "CITY_NAME";
    private static final String TAG = "demo";

    private Data.City city;

    TextView temperature, temperatureMax, temperatureMin, description, humidity, windSpeed, windDegree, cloudiness, cityNameLabel;
    ImageView currentForecastImage;
    Button checkForeCastButton;

    ICurrentWeatherForecastListener listener;

    ProgressDialog progressDialog;

    OkHttpClient client = new OkHttpClient();

    public CurrentWeatherFragment() {

    }

    public static CurrentWeatherFragment newInstance(Data.City cityName) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = (Data.City) getArguments().getSerializable(CITY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);

        getActivity().setTitle(getString(R.string.current_weather_title));

        cityNameLabel = view.findViewById(R.id.cityNameCurrentWeather);
        temperature = view.findViewById(R.id.currentWeatherTemparatureValue);
        temperatureMax = view.findViewById(R.id.currentWeatherTemparatureHighValue);
        temperatureMin = view.findViewById(R.id.currentWeatherTemparatureMinValue);
        description = view.findViewById(R.id.currentWeatherDescriptionValue);
        humidity = view.findViewById(R.id.currentWeatherHumidityValue);
        windSpeed = view.findViewById(R.id.currentWeatherWindSpeedValue);
        windDegree = view.findViewById(R.id.currentWeatherWindDegreeValue);
        cloudiness = view.findViewById(R.id.currentWeatherCloudlinessValue);
        currentForecastImage = view.findViewById(R.id.currentWeatherView);
        checkForeCastButton = view.findViewById(R.id.checkForecastButton);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.show();

        cityNameLabel.setText(city.getCity()+", "+ city.getCountry());

        Log.d(TAG, "onCreateView: "+Thread.currentThread().getId());

        HttpUrl url = new HttpUrl.Builder().scheme(getString(R.string.schema_host))
                .host(getString(R.string.weather_host))
                .addPathSegment(getString(R.string.weather_current_path))
                .addQueryParameter("q", city.getCity())
                .addQueryParameter("appid", getString(R.string.api_token))
                .addQueryParameter("units", "imperial")
                .build();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Gson gson = new Gson();
                WeatherResponse weatherResponse = gson.fromJson(response.body().string(), WeatherResponse.class);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "runOnUiThread: "+Thread.currentThread().getId());
                        if (weatherResponse != null) {
                            if (weatherResponse.getMain() != null) {
                                temperature.setText(kelvinToFarenheit(weatherResponse.getMain().getTemp()) + " F");
                                temperatureMax.setText(kelvinToFarenheit(weatherResponse.getMain().getTemp_max()) + " F");
                                temperatureMin.setText(kelvinToFarenheit(weatherResponse.getMain().getTemp_min()) + " F");
                                humidity.setText(weatherResponse.getMain().getHumidity() + " %");
                            }
                            if (weatherResponse.getWind() != null) {
                                windSpeed.setText(weatherResponse.getWind().getSpeed() + " "+getString(R.string.mph));
                                windDegree.setText(weatherResponse.getWind().getDeg() + " "+getString(R.string.degrees));
                            }

                            if (weatherResponse.getClouds() != null) {
                                cloudiness.setText(weatherResponse.getClouds().getAll() + " %");
                            }

                            if (weatherResponse.getWeather() != null && weatherResponse.getWeather().size() > 0) {
                                description.setText(weatherResponse.getWeather().get(0).getDescription());
                                Picasso.get().load(getString(R.string.picaso_url)+weatherResponse.getWeather().get(0).getIcon()+".png")
                                        .fit()
                                        .centerCrop()
                                        .into(currentForecastImage);
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });

        checkForeCastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.callWeatherForecast(city);
            }
        });

        return view;
    }

    public String kelvinToFarenheit(float kelvin) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(kelvin);
    }

    class CallAsyncWeatherForeCast extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ICurrentWeatherForecastListener){
            listener = (ICurrentWeatherForecastListener) context;
        }
    }

    interface ICurrentWeatherForecastListener{
        void callWeatherForecast(Data.City cityName);
    }
}