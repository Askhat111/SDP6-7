package org.example.weather.observer;

import org.example.weather.model.WeatherData;
import org.example.weather.model.User;

public class EmailObserver implements WeatherObserver {
    @Override
    public void update(User user, WeatherData weather) {
        System.out.println("📧 Email to " + user.getEmail() +
                ": Weather update - " + weather.getCondition() +
                " in " + weather.getCity());
    }

    @Override
    public String getObserverType() {
        return "Email";
    }
}