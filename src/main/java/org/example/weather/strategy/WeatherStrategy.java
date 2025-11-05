package org.example.weather.strategy;

import org.example.weather.model.WeatherData;

public interface WeatherStrategy {
    WeatherData getWeather(String city);
    String getStrategyName();
}
