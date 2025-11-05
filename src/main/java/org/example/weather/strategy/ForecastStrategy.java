package org.example.weather.strategy;

import org.example.weather.model.WeatherData;
import java.util.Arrays;
import java.util.List;

public class ForecastStrategy implements WeatherStrategy {

    @Override
    public WeatherData getWeather(String city) {
        // Forecast: More stable and predictable data
        int temp = getForecastTemp(city);
        int humidity = getStableHumidity(city);
        int windSpeed = getStableWindSpeed(city);
        String condition = getForecastCondition(city);

        // Forecast shows predicted temperatures for next 3 days
        List<Integer> forecastTemps = getThreeDayForecast(city);

        return WeatherData.builder()
                .city(city)
                .temperature(temp)
                .humidity(humidity)
                .windSpeed(windSpeed)  // This should work now
                .condition(condition + " (Forecast)")
                .temperatureHistory(forecastTemps)
                .build();
    }

    private int getForecastTemp(String city) {
        return switch (city.toLowerCase()) {
            case "astana" -> -3; // More moderate than real-time
            case "karaganda" -> 2;
            case "almaty" -> 8;
            default -> 12;
        };
    }

    private int getStableHumidity(String city) {
        // Forecast humidity is more stable
        return switch (city.toLowerCase()) {
            case "astana" -> 75;
            case "karaganda" -> 70;
            case "almaty" -> 65;
            default -> 60;
        };
    }

    private int getStableWindSpeed(String city) {
        // Forecast wind is more predictable
        return switch (city.toLowerCase()) {
            case "astana" -> 15;
            case "karaganda" -> 12;
            case "almaty" -> 8;
            default -> 10;
        };
    }

    private String getForecastCondition(String city) {
        // Forecast conditions are more predictable
        return switch (city.toLowerCase()) {
            case "astana" -> "Partly Cloudy";
            case "karaganda" -> "Mostly Sunny";
            case "almaty" -> "Clear";
            default -> "Sunny";
        };
    }

    private List<Integer> getThreeDayForecast(String city) {
        // Returns temperatures for today, tomorrow, day after tomorrow
        return switch (city.toLowerCase()) {
            case "astana" -> Arrays.asList(-3, -2, -4);
            case "karaganda" -> Arrays.asList(2, 3, 1);
            case "almaty" -> Arrays.asList(8, 9, 7);
            default -> Arrays.asList(12, 13, 11);
        };
    }

    @Override
    public String getStrategyName() {
        return "3-Day Forecast";
    }
}