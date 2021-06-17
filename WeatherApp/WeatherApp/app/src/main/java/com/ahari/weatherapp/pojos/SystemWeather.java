package com.ahari.weatherapp.pojos;

/*
    HW05
    SystemWeather
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class SystemWeather {
    String country;
    int type, id;
    long sunrise, sunset;

    @Override
    public String toString() {
        return "SystemWeather{" +
                "country='" + country + '\'' +
                ", type=" + type +
                ", id=" + id +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
}
