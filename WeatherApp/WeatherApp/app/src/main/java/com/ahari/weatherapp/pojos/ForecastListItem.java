package com.ahari.weatherapp.pojos;

import java.util.ArrayList;

/*
    HW05
    ForecastListItem
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class ForecastListItem {
    MainWeatherDetails main;
    ArrayList<Weather> weather;
    Clouds clouds;
    Wind wind;
    int visibility;
    float pop;
    Object rain;
    SystemForecastWeather sys;
    String dt_txt;

    public MainWeatherDetails getMain() {
        return main;
    }

    public void setMain(MainWeatherDetails main) {
        this.main = main;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public float getPop() {
        return pop;
    }

    public void setPop(float pop) {
        this.pop = pop;
    }

    public Object getRain() {
        return rain;
    }

    public void setRain(Object rain) {
        this.rain = rain;
    }

    @Override
    public String toString() {
        return "ForecastListItem{" +
                "main=" + main +
                ", weather=" + weather +
                ", clouds=" + clouds +
                ", wind=" + wind +
                ", visibility=" + visibility +
                ", pop=" + pop +
                ", rain=" + rain +
                ", sys=" + sys +
                ", dt_txt='" + dt_txt + '\'' +
                '}';
    }

    public SystemForecastWeather getSys() {
        return sys;
    }

    public void setSys(SystemForecastWeather sys) {
        this.sys = sys;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
