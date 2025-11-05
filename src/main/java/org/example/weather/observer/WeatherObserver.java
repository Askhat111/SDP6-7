package org.example.weather.observer;

import org.example.weather.model.WeatherData;
import org.example.weather.model.User;

public interface WeatherObserver {
    void update(User user, WeatherData weather);
    String getObserverType();
}






