package org.example.weather.observer;

import org.example.weather.model.WeatherData;
import org.example.weather.model.User;

public class MobileAppObserver implements WeatherObserver {
    @Override
    public void update(User user, WeatherData weather) {
        System.out.println("📱 Mobile App for " + user.getName() +
                ": " + weather.getCondition() + " in " + weather.getCity() +
                " (" + weather.getTemperature() + "°C)");
    }

    @Override
    public String getObserverType() {
        return "Mobile App";
    }
}





