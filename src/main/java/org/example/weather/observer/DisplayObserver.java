package org.example.weather.observer;

import org.example.weather.model.WeatherData;
import org.example.weather.model.User;

public class DisplayObserver implements WeatherObserver {
    @Override
    public void update(User user, WeatherData weather) {
        System.out.println("🖥️  Display for " + user.getName() +
                ": " + weather.getCity() + " - " +
                weather.getTemperature() + "°C, " +
                weather.getHumidity() + "% humidity");
    }

    @Override
    public String getObserverType() {
        return "Display";
    }
}



