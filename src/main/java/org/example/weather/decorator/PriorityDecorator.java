package org.example.weather.decorator;

import org.example.weather.observer.WeatherObserver;
import org.example.weather.model.WeatherData;
import org.example.weather.model.User;

public class PriorityDecorator extends WeatherObserverDecorator {

    public PriorityDecorator(WeatherObserver observer) {
        super(observer);
    }

    @Override
    public void update(User user, WeatherData weather) {

        if (isHighPriority(user, weather)) {
            System.out.println("PRIORITY update for " + user.getName());
        }

        super.update(user, weather);
    }

    private boolean isHighPriority(User user, WeatherData weather) {

        return user.getSubscribedCities().size() > 2 ||
                weather.getTemperature() > 30 || weather.getTemperature() < 0;
    }

    @Override
    public String getObserverType() {
        return "Priority-" + super.getObserverType();
    }
}