package org.example.weather.strategy;

import org.example.weather.model.WeatherData;

public interface UpdateStrategy {
    WeatherData fetchWeather(String city);
}

