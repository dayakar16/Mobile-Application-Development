package com.ahari.weatherapp.pojos;

/*
    HW05
    CoOrd
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class CoOrd {
    float lon, lat;

    public float getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "CoOrd{" +
                "lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
