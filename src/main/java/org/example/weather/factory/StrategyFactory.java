package org.example.weather.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.example.weather.strategy.*;
import org.example.weather.user.*;

@Component
public class WeatherStrategyFactory {

    @Autowired
    private ScheduledUpdateStrategy scheduled;
    @Autowired
    private ManualInputStrategy manual;
    @Autowired
    private RealTimeUpdateStrategy realtime;

    public WeatherStrategy getStrategy(UpdateStrategyType type) {
        switch (type) {
            case SCHEDULED:
                return scheduled;
            case MANUAL:
                return manual;
            case REALTIME:
                return realtime;
            default:
                throw new IllegalArgumentException("Unknown strategy type: " + type);
        }
    }
}
