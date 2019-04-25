package com.cdtekk.firebaseapp.Model;

public class SensorData {
    private int fan1;
    private int fan2;
    private float humidity;
    private float temperature;
    private String time;

    public int getFan1() { return fan1; }

    public int getFan2() { return fan2; }

    public float getHumidity() {
        return humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public String getTime() { return time; }

    public SensorData() {}

    public SensorData(int fan1, int fan2, float humidity, float temperature, String time) {
        this.fan1 = fan1;
        this.fan2 = fan2;
        this.humidity = humidity;
        this.temperature = temperature;
        this.time = time;
    }
}
