package com.ahari.weatherapp.pojos;

/*
    HW05
    Wind
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class Wind {
    float speed;
    int deg;

    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                ", deg=" + deg +
                '}';
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }
}
