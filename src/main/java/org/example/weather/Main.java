package org.example;

import org.example.weather.model.WeatherData;
import org.example.weather.MobileDisplay;
import org.example.weather.WeatherDisplay;
import org.example.weather.builder.WeatherConfigBuilder;
import org.example.weather.decorator.LoggingObserver;
import org.example.weather.observer.*;
import org.example.weather.strategy.ApiUpdateStrategy;
import org.example.weather.strategy.ManualInputStrategy;

public class Main {
    public static void main(String[] args) {
        // 1. Build WeatherStation with initial strategy
        WeatherStation station = new WeatherConfigBuilder()
                .setStrategy(new ApiUpdateStrategy())
                .build();

        // 2. Create observers
        Observer phoneObs = new LoggingObserver(new PhoneObserver("Alex"));
        Observer webObs = new WebWidgetObserver("Widget1");
        Observer appObs = new AppObserver("WeatherApp");

        // 3. Register observers for cities
        station.registerObserver("London", phoneObs);
        station.registerObserver("London", webObs);
        station.registerObserver("Paris", appObs);

        // 4. Notify observers (simulate weather update)
        station.notifyObservers("London");
        station.notifyObservers("Paris");

        // 5. Switch strategy to manual input (simulate input)
        station.setStrategy(new ManualInputStrategy());
        station.notifyObservers("London");

        // 6. Show Bridge pattern
        WeatherDisplay mobileDisplay = new MobileDisplay();
        WeatherData data = new ApiUpdateStrategy().fetchWeather("Berlin");
        mobileDisplay.showWeather(data);
    }
}
