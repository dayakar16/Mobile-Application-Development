package com.ahari.weatherapp.pojos;

/*
    HW05
    ForecastCity
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class ForecastCity {
    String name, country;
    CoOrd coord;
    long population, timezone, sunrise, sunset;

    @Override
    public String toString() {
        return "ForecastCity{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", coord=" + coord +
                ", population=" + population +
                ", timezone=" + timezone +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public CoOrd getCoord() {
        return coord;
    }

    public void setCoord(CoOrd coord) {
        this.coord = coord;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public long getTimezone() {
        return timezone;
    }

    public void setTimezone(long timezone) {
        this.timezone = timezone;
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
