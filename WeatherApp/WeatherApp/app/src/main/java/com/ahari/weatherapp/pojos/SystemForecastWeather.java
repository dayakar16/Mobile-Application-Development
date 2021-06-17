package com.ahari.weatherapp.pojos;

/*
    HW05
    SystemForecastWeather
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class SystemForecastWeather {
    String pod;

    @Override
    public String toString() {
        return "SystemForecastWeather{" +
                "pod='" + pod + '\'' +
                '}';
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }
}
