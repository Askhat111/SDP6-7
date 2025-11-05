package org.example.weather.strategy;

import org.example.weather.model.WeatherData;
import java.util.Arrays;

public class ManualStrategy implements WeatherStrategy {
    private WeatherData manualData;

    public ManualStrategy() {

        this.manualData = WeatherData.builder()
                .city("Manual City")
                .temperature(25)
                .humidity(45)
                .windSpeed(5)
                .condition("Manual Entry")
                .temperatureHistory(Arrays.asList(24, 25, 26))
                .build();
    }

    public void setManualData(WeatherData data) {
        this.manualData = data;
    }

    @Override
    public WeatherData getWeather(String city) {

        return WeatherData.builder()
                .city(city + " (Manual)")
                .temperature(manualData.getTemperature())
                .humidity(manualData.getHumidity())
                .windSpeed(manualData.getWindSpeed())
                .condition(manualData.getCondition())
                .temperatureHistory(manualData.getTemperatureHistory())
                .build();
    }

    @Override
    public String getStrategyName() {
        return "Manual Input";
    }
}




