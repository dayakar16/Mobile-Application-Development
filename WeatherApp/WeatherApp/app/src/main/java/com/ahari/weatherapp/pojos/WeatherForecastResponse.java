package com.ahari.weatherapp.pojos;

import java.util.ArrayList;

/*
    HW05
    WeatherForecastResponse
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class WeatherForecastResponse {
    String cod;
    int message, cnt;
    ArrayList<ForecastListItem> list;
    ForecastCity city;

    @Override
    public String toString() {
        return "WeatherForecastResponse{" +
                "cod='" + cod + '\'' +
                ", message=" + message +
                ", cnt=" + cnt +
                ", list=" + list +
                ", city=" + city +
                '}';
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public ArrayList<ForecastListItem> getList() {
        return list;
    }

    public void setList(ArrayList<ForecastListItem> list) {
        this.list = list;
    }

    public ForecastCity getCity() {
        return city;
    }

    public void setCity(ForecastCity city) {
        this.city = city;
    }
}
