package org.example.weather.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeatherData {
    private final String city;
    private final int temperature;
    private final int humidity;
    private final int windSpeed;
    private final String condition;
    private final LocalDateTime timestamp;
    private final List<Integer> temperatureHistory;

    private WeatherData(Builder builder) {
        this.city = builder.city;
        this.temperature = builder.temperature;
        this.humidity = builder.humidity;
        this.windSpeed = builder.windSpeed;
        this.condition = builder.condition;
        this.timestamp = builder.timestamp;
        this.temperatureHistory = new ArrayList<>(builder.temperatureHistory);
    }

    public String getCity() { return city; }
    public int getTemperature() { return temperature; }
    public int getHumidity() { return humidity; }
    public int getWindSpeed() { return windSpeed; }
    public String getCondition() { return condition; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<Integer> getTemperatureHistory() { return new ArrayList<>(temperatureHistory); }

    @Override
    public String toString() {
        return String.format("WeatherData{city='%s', temperature=%d°C, humidity=%d%%, windSpeed=%d km/h, condition='%s', timestamp=%s}",
                city, temperature, humidity, windSpeed, condition, timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherData that = (WeatherData) o;
        return temperature == that.temperature &&
                humidity == that.humidity &&
                windSpeed == that.windSpeed &&
                Objects.equals(city, that.city) &&
                Objects.equals(condition, that.condition) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, temperature, humidity, windSpeed, condition, timestamp);
    }

    public static class Builder {
        private String city;
        private int temperature;
        private int humidity;
        private int windSpeed;
        private String condition = "Sunny";
        private LocalDateTime timestamp = LocalDateTime.now();
        private List<Integer> temperatureHistory = new ArrayList<>();

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder temperature(int temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder humidity(int humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder windSpeed(int windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder condition(String condition) {
            this.condition = condition;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder temperatureHistory(List<Integer> temperatureHistory) {
            this.temperatureHistory = temperatureHistory != null ?
                    new ArrayList<>(temperatureHistory) : new ArrayList<>();
            return this;
        }

        public WeatherData build() {
            if (city == null || city.isBlank()) {
                throw new IllegalStateException("City is required");
            }
            return new WeatherData(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}





