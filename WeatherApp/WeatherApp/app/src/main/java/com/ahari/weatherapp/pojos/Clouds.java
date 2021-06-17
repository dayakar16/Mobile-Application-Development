package com.ahari.weatherapp.pojos;

/*
    HW05
    WeatherForecastFragment
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class Clouds {
    int all;

    @Override
    public String toString() {
        return "Clouds{" +
                "all=" + all +
                '}';
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}
