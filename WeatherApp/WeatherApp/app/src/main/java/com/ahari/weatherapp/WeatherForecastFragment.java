package com.ahari.weatherapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahari.weatherapp.pojos.ForecastListItem;
import com.ahari.weatherapp.pojos.MainWeatherDetails;
import com.ahari.weatherapp.pojos.Weather;
import com.ahari.weatherapp.pojos.WeatherForecastResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
    HW05
    WeatherForecastFragment
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class WeatherForecastFragment extends Fragment {

    private static final String CITY_NAME = "CITY_NAME";

    private Data.City cityName;

    TextView cityNameWeatherForecast;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ForecastAdapter adapter;

    ProgressDialog progressDialog;

    ArrayList<ForecastListItem> list = new ArrayList<>();

    OkHttpClient client = new OkHttpClient();

    public WeatherForecastFragment() {

    }

    public static WeatherForecastFragment newInstance(Data.City cityName) {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityName = (Data.City) getArguments().getSerializable(CITY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_forecast, container, false);

        getActivity().setTitle(getString(R.string.forecast_weather_title));

        cityNameWeatherForecast = view.findViewById(R.id.cityNameWeatherForecast);
        recyclerView = view.findViewById(R.id.weatherForecastRecyclerView);
        cityNameWeatherForecast.setText(cityName.getCity()+", "+cityName.getCountry());
        adapter = new ForecastAdapter();
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.show();

        HttpUrl url = new HttpUrl.Builder().scheme(getString(R.string.schema_host))
                .host(getString(R.string.weather_host))
                .addPathSegment(getString(R.string.weather_forecast_path))
                .addQueryParameter("q", cityName.getCity())
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
                        Toast.makeText(getActivity(), getString(R.string.error_toast), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Gson gson = new Gson();
                WeatherForecastResponse weatherForecastResponse = gson.fromJson(response.body().string(), WeatherForecastResponse.class);
                list.addAll(weatherForecastResponse.getList());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

    class ForecasViewHolder extends RecyclerView.ViewHolder {

        private TextView date, temp, tempMax, tempMin, humidity, description;
        private ImageView descImg;
        private ConstraintLayout box;
        String iconName;

        public ForecasViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.forecastItemTime);
            temp = itemView.findViewById(R.id.forecastItemTemparature);
            tempMax = itemView.findViewById(R.id.forecastItemTempMaxValue);
            tempMin = itemView.findViewById(R.id.forecastItemTempMinValue);
            humidity = itemView.findViewById(R.id.forecastItemHumidityValue);
            description = itemView.findViewById(R.id.forecastItemDescription);
            descImg = itemView.findViewById(R.id.forecastItemImageView);
            box = itemView.findViewById(R.id.forecastItemBox);
        }
    }

    class ForecastAdapter extends RecyclerView.Adapter<ForecasViewHolder> {

        @NonNull
        @Override
        public ForecasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_forecast_list_item, parent, false);
            ForecasViewHolder holder = new ForecasViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ForecasViewHolder holder, int position) {
            ForecastListItem listItem = list.get(position);
            if (listItem != null) {
                if (listItem.getWeather() != null && listItem.getWeather().size() > 0) {
                    Weather weather = new Weather();
                    weather = listItem.getWeather().get(0);
                    holder.iconName = weather.getIcon();
                    holder.description.setText(weather.getDescription());
                }
                if (listItem.getMain() != null) {
                    MainWeatherDetails main = listItem.getMain();
                    holder.temp.setText(kelvinToFarenheit(main.getTemp()) + " F");
                    holder.tempMax.setText(kelvinToFarenheit(main.getTemp_max()) + " F");
                    holder.tempMin.setText(kelvinToFarenheit(main.getTemp_min()) + " F");
                    holder.humidity.setText(main.getHumidity()+" %");
                }
                holder.box.setBackground(getResources().getDrawable(R.drawable.border));
                holder.date.setText(listItem.getDt_txt());
                Picasso.get().load(getString(R.string.picaso_url) + holder.iconName + ".png")
                        .into(holder.descImg);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public String kelvinToFarenheit(float kelvin) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(kelvin);
    }
}