package org.example.weather.strategy;

import org.example.weather.model.WeatherData;
import java.util.Random;
import java.util.Arrays;
import java.util.List;

public class RealTimeStrategy implements WeatherStrategy {
    private Random random = new Random();

    @Override
    public WeatherData getWeather(String city) {

        int temp = getCityBaseTemp(city) + random.nextInt(15) - 7;
        int humidity = random.nextInt(50) + 30;
        int windSpeed = random.nextInt(40) + 5;
        String condition = getRandomCondition();


        List<Integer> history = generateRealisticHistory(temp);

        return WeatherData.builder()
                .city(city)
                .temperature(temp)
                .humidity(humidity)
                .windSpeed(windSpeed)
                .condition(condition)
                .temperatureHistory(history)
                .build();
    }

    private int getCityBaseTemp(String city) {
        return switch (city.toLowerCase()) {
            case "astana" -> -5;
            case "karaganda" -> -1;
            case "almaty" -> 6;
            default -> 10;
        };
    }

    private String getRandomCondition() {
        String[] conditions = {"Sunny", "Cloudy", "Rainy", "Snowy", "Windy", "Foggy"};
        return conditions[random.nextInt(conditions.length)];
    }

    private List<Integer> generateRealisticHistory(int currentTemp) {
        Integer[] history = new Integer[6];
        history[5] = currentTemp;
        for (int i = 4; i >= 0; i--) {
            history[i] = history[i + 1] + random.nextInt(5) - 2;
        }
        return Arrays.asList(history);
    }

    @Override
    public String getStrategyName() {
        return "Real-Time API";
    }
}






