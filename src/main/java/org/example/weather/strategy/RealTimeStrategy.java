package org.example.weather.strategy;

import org.springframework.stereotype.Component;
import org.example.weather.model.WeatherData;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Component
public class RealTimeUpdateStrategy implements WeatherStrategy {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "ea55e77818b2c1fc8ef4b8a8014319ce";

    @Override
    public WeatherData fetchWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city
                + "&appid=" + apiKey + "&units=metric";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null) throw new RuntimeException("API returned null response");

        Map<String, Object> main = (Map<String, Object>) response.get("main");
        int temp = ((Number) main.get("temp")).intValue();
        int humidity = ((Number) main.get("humidity")).intValue();

        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
        String forecast = weatherList.get(0).get("main").toString();

        Map<String, Object> windMap = (Map<String, Object>) response.get("wind");
        int windSpeed = ((Number) windMap.get("speed")).intValue();

        return new WeatherData(city, temp, humidity, windSpeed, forecast, List.of(temp));
    }
}







