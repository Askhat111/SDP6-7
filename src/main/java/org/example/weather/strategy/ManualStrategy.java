package org.example.weather.strategy;

import org.springframework.stereotype.Component;
import org.example.weather.model.WeatherData;
import java.util.*;

@Component
public class ManualInputStrategy implements WeatherStrategy {

    private WeatherData manualData = new WeatherData(
            "Manual City", 0, 0, 0, "Manual", Arrays.asList(0,0,0,0,0,0,0)
    );

    // Для ручного ввода через POST /api/weather/manual
    public void updateData(WeatherData newData) {
        this.manualData = newData;
    }

    @Override
    public WeatherData fetchWeather(String city) {
        // Аргумент city можно игнорировать или возвращать manualData.getCity()
        return this.manualData;
    }
}




