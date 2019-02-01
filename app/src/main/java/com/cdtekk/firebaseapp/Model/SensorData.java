package com.cdtekk.firebaseapp.Model;

public class SensorData {
    private float humidity;
    private float temperature;
    private String time;

    public float getHumidity() {
        return humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public String getTime() {
        return time;
    }

    public SensorData() {}

    public SensorData(float humidity, float temperature, String time) {
        this.humidity = humidity;
        this.temperature = temperature;
        this.time = time;
    }
}
