package org.example.weather.core;

import org.example.weather.strategy.WeatherStrategy;
import org.example.weather.observer.*;
import org.example.weather.model.WeatherData;
import org.example.weather.model.User;

import java.util.*;

public class WeatherStation {
    private WeatherStrategy currentStrategy;
    private Map<String, List<WeatherObserver>> cityObservers = new HashMap<>();
    private Map<String, User> users = new HashMap<>();

    public WeatherStation(WeatherStrategy initialStrategy) {
        this.currentStrategy = initialStrategy;
    }

    public void setStrategy(WeatherStrategy strategy) {
        this.currentStrategy = strategy;
        System.out.println("Switched to: " + strategy.getStrategyName());
    }

    public void registerUser(User user) {
        users.put(user.getName(), user);

        for (String city : user.getSubscribedCities()) {
            registerObserver(city, new MobileAppObserver());
            registerObserver(city, new EmailObserver());
        }
    }

    public void registerObserver(String city, WeatherObserver observer) {
        cityObservers.computeIfAbsent(city, k -> new ArrayList<>()).add(observer);
    }

    public void updateWeather(String city) {
        WeatherData weather = currentStrategy.getWeather(city);
        notifyObservers(city, weather);
    }

    private void notifyObservers(String city, WeatherData weather) {
        List<WeatherObserver> observers = cityObservers.getOrDefault(city, new ArrayList<>());

        for (User user : users.values()) {
            if (user.isSubscribedTo(city)) {
                for (WeatherObserver observer : observers) {
                    observer.update(user, weather);
                }
            }
        }
    }

    public void updateAllCities() {
        Set<String> allCities = new HashSet<>();
        for (User user : users.values()) {
            allCities.addAll(user.getSubscribedCities());
        }

        for (String city : allCities) {
            updateWeather(city);
        }
    }

    public WeatherStrategy getCurrentStrategy() {
        return currentStrategy;
    }

    public List<String> getSubscribedCities() {
        Set<String> cities = new HashSet<>();
        for (User user : users.values()) {
            cities.addAll(user.getSubscribedCities());
        }
        return new ArrayList<>(cities);
    }
}