package org.example.weather.decorator;

import org.example.weather.observer.WeatherObserver;
import org.example.weather.model.WeatherData;
import org.example.weather.model.User;

public class AlertDecorator extends WeatherObserverDecorator {

    public AlertDecorator(WeatherObserver observer) {
        super(observer);
    }

    @Override
    public void update(User user, WeatherData weather) {

        if (isSevereWeather(weather)) {
            System.out.println("ALERT for " + user.getName() +
                    ": Severe weather in " + weather.getCity() +
                    " - " + weather.getCondition());
        }

        super.update(user, weather);
    }

    private boolean isSevereWeather(WeatherData weather) {
        return weather.getCondition().equals("Snowy") ||
                weather.getCondition().equals("Rainy") && weather.getTemperature() < 5;
    }

    @Override
    public String getObserverType() {
        return "Alert-" + super.getObserverType();
    }
}