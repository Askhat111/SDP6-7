package org.example.weather.decorator;

import org.example.weather.observer.Observer;
import org.example.weather.user.User;
import org.example.weather.model.WeatherData;

public abstract class ObserverDecorator implements Observer {
    protected Observer wrappee;

    public ObserverDecorator(Observer wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public void update(User user, WeatherData data) {
        wrappee.update(user, data);
    }
}



