package org.example.weather.observer;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.weather.service.UserService;
import org.example.weather.strategy.WeatherStrategy;
import org.example.weather.strategy.ManualInputStrategy;
import org.example.weather.strategy.RealTimeUpdateStrategy;
import org.example.weather.model.WeatherData;
import org.example.weather.user.User;

import java.util.*;

@Component
public class WeatherStation {
    private final Map<String, List<Observer>> cityObservers = new HashMap<>();
    private final Map<String, WeatherData> cityWeather = new HashMap<>();
    private final UserService userService;
    private WeatherStrategy strategy;
    private final Map<String, WeatherStrategy> strategies = new HashMap<>();

    @Autowired
    public WeatherStation(UserService userService,
                          ManualInputStrategy manualStrategy,
                          RealTimeUpdateStrategy apiStrategy,
                          ScheduledUpdateStrategy scheduledStrategy) {
        this.userService = userService;
        strategies.put("manual", manualStrategy);
        strategies.put("api", apiStrategy);
        strategies.put("scheduled", scheduledStrategy);
        this.strategy = manualStrategy;
    }

    public void setStrategy(String strategyName) {
        this.strategy = strategies.getOrDefault(strategyName, strategies.get("manual"));
    }

    public WeatherData updateWeather(String city) {
        if (strategy == null) throw new IllegalStateException("Strategy not set");
        WeatherData data = strategy.fetchWeather(city);
        manualUpdate(city, data);
        return data;
    }

    public void manualUpdate(String city, WeatherData data) {
        cityWeather.put(city, data);
        notifyObservers(city);
    }

    public void registerObserver(String city, Observer observer) {
        cityObservers.computeIfAbsent(city, k -> new ArrayList<>()).add(observer);
    }

    public void notifyObservers(String city) {
        WeatherData data = cityWeather.get(city);
        if (data == null) return;
        List<Observer> obsList = cityObservers.getOrDefault(city, List.of());
        Set<User> users = userService.getUsersSubscribedToCity(city);
        for (User user : users) {
            for (Observer obs : obsList) {
                obs.update(user, data);
            }
        }
    }
}






