package org.example.weather;

import org.example.weather.core.WeatherStation;
import org.example.weather.factory.StrategyFactory;
import org.example.weather.observer.*;
import org.example.weather.decorator.*;
import org.example.weather.model.User;
import org.example.weather.model.WeatherData;

public class Main {
    public static void main(String[] args) {
        System.out.println("🌤️ Kazakhstan Weather System ");
        System.out.println("\n");

        WeatherStation station = new WeatherStation(StrategyFactory.createStrategy("realtime"));

        User user1 = User.builder()
                .name("Asyl")
                .email("asyl@email.com")
                .subscribeTo("Astana")
                .subscribeTo("Almaty")
                .build();

        User user2 = User.builder()
                .name("Bekzat")
                .email("bekzat@email.com")
                .subscribeTo("Karaganda")
                .build();

        station.registerUser(user1);
        station.registerUser(user2);

        station.registerObserver("Astana", new AlertDecorator(new MobileAppObserver()));
        station.registerObserver("Almaty", new PriorityDecorator(new EmailObserver()));
        station.registerObserver("Karaganda", new AlertDecorator(new PriorityDecorator(new DisplayObserver())));

        System.out.println("📋 Subscribed Cities: " + station.getSubscribedCities());

        System.out.println("\nRealTime Strategy:");
        testStrategy(station, "realtime");

        System.out.println("\nForecast Strategy:");
        testStrategy(station, "forecast");

        System.out.println("\nManual Strategy:");
        testStrategy(station, "manual");
    }

    private static void testStrategy(WeatherStation station, String strategyName) {
        station.setStrategy(StrategyFactory.createStrategy(strategyName));
        station.updateAllCities();

        for (String city : station.getSubscribedCities()) {
            WeatherData data = station.getCurrentStrategy().getWeather(city);
            System.out.printf("  %s: %d°C, %d%% humidity, %d km/h wind, %s%n",
                    data.getCity(), data.getTemperature(), data.getHumidity(),
                    data.getWindSpeed(), data.getCondition());
        }
    }
}






