package org.example.weather.factory;

import org.example.weather.strategy.*;

public class StrategyFactory {
    public static WeatherStrategy createStrategy(String type) {
        switch (type.toLowerCase()) {
            case "realtime":
                return new RealTimeStrategy();
            case "forecast":
                return new ForecastStrategy();
            case "manual":
                return new ManualStrategy();
            default:
                throw new IllegalArgumentException("Unknown strategy: " + type);
        }
    }

    public static String[] getAvailableStrategies() {
        return new String[]{"realtime", "forecast", "manual"};
    }
}