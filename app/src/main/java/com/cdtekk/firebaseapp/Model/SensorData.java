package com.cdtekk.firebaseapp.Model;

public class SensorData {
    private float humidity;
    private float temperature;
    private String time;
    private int fan1State;
    private int fan2State;

    public float getHumidity() {
        return humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public int getFan1State() { return fan1State; }

    public int getFan2State() { return fan2State; }

    public String getTime() {
        return time;
    }

    public SensorData() {}

    public SensorData(float humidity, float temperature, String time, int fan1State, int fan2State) {
        this.humidity = humidity;
        this.temperature = temperature;
        this.time = time;
        this.fan1State = fan1State;
        this.fan2State = fan2State;
    }
}
