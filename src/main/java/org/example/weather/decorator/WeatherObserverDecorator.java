package org.example.weather.decorator;

import org.example.weather.observer.WeatherObserver;
import org.example.weather.model.WeatherData;
import org.example.weather.model.User;

public abstract class WeatherObserverDecorator implements WeatherObserver {
    protected WeatherObserver decoratedObserver;

    public WeatherObserverDecorator(WeatherObserver observer) {
        this.decoratedObserver = observer;
    }

    @Override
    public void update(User user, WeatherData weather) {
        decoratedObserver.update(user, weather);
    }

    @Override
    public String getObserverType() {
        return decoratedObserver.getObserverType();
    }
}



