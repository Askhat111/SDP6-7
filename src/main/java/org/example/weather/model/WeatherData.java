package org.example.model;

public class WeatherData {
    private final String city;
    private final double temperature;
    private final int humidity;

    public WeatherData(String city, double temperature, int humidity) {
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getCity() { return city; }
    public double getTemperature() { return temperature; }
    public int getHumidity() { return humidity; }

    @Override
    public String toString() {
        return "WeatherData{" +
                "city='" + city + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                '}';
    }
}
