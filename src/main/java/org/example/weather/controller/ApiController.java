package org.example.weather.controller;

import org.example.weather.core.WeatherStation;
import org.example.weather.factory.StrategyFactory;
import org.example.weather.model.User;
import org.example.weather.observer.*;
import org.example.weather.decorator.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    private WeatherStation weatherStation;

    public ApiController() {

        this.weatherStation = new WeatherStation(StrategyFactory.createStrategy("realtime"));

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

        weatherStation.registerUser(user1);
        weatherStation.registerUser(user2);

        weatherStation.registerObserver("Astana", new AlertDecorator(new MobileAppObserver()));
        weatherStation.registerObserver("Almaty", new PriorityDecorator(new EmailObserver()));
        weatherStation.registerObserver("Karaganda", new AlertDecorator(new PriorityDecorator(new DisplayObserver())));
    }

    @PostMapping("/change-strategy")
    public ResponseEntity<Map<String, String>> changeStrategy(@RequestParam String strategy) {
        try {
            weatherStation.setStrategy(StrategyFactory.createStrategy(strategy));
            Map<String, String> response = new HashMap<>();
            response.put("message", "Strategy changed to: " + strategy);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to change strategy: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/update-all")
    public ResponseEntity<Map<String, String>> updateAllCities() {
        try {
            weatherStation.updateAllCities();
            Map<String, String> response = new HashMap<>();
            response.put("message", "All cities updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to update cities: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/update-weather")
    public ResponseEntity<Map<String, String>> updateWeather(@RequestParam String city) {
        try {
            weatherStation.updateWeather(city);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Weather updated for: " + city);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to update city: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/add-user")
    public ResponseEntity<Map<String, String>> addUser(@RequestParam String name,
                                                       @RequestParam String email,
                                                       @RequestParam String city) {
        try {
            User user = User.builder()
                    .name(name)
                    .email(email)
                    .subscribeTo(city)
                    .build();
            weatherStation.registerUser(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "User " + name + " added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to add user: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/cities")
    public ResponseEntity<Map<String, Object>> getCities() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("cities", weatherStation.getSubscribedCities());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Failed to get cities: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}