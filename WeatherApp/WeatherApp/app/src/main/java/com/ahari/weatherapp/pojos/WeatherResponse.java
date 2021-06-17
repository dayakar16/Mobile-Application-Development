package com.ahari.weatherapp.pojos;

import java.util.ArrayList;

/*
    HW05
    WeatherResponse
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class WeatherResponse {
    String base, name;
    int visibility, dt, timezone, id, cod;
    CoOrd coord;
    ArrayList<Weather> weather;
    MainWeatherDetails main;
    Wind wind;
    Clouds clouds;
    SystemWeather sys;

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "base='" + base + '\'' +
                ", name='" + name + '\'' +
                ", visibility=" + visibility +
                ", dt=" + dt +
                ", timezone=" + timezone +
                ", id=" + id +
                ", cod=" + cod +
                ", coord=" + coord +
                ", weather=" + weather +
                ", main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", sys=" + sys +
                '}';
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public CoOrd getCoord() {
        return coord;
    }

    public void setCoord(CoOrd coord) {
        this.coord = coord;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public MainWeatherDetails getMain() {
        return main;
    }

    public void setMain(MainWeatherDetails main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public SystemWeather getSys() {
        return sys;
    }

    public void setSys(SystemWeather sys) {
        this.sys = sys;
    }
}
