package org.example.weather.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String name;
    private final String email;
    private final List<String> subscribedCities;

    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.subscribedCities = builder.subscribedCities;
    }


    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getSubscribedCities() { return new ArrayList<>(subscribedCities); }

    public void subscribeToCity(String city) {
        if (!subscribedCities.contains(city)) {
            subscribedCities.add(city);
        }
    }

    public boolean isSubscribedTo(String city) {
        return subscribedCities.contains(city);
    }

    public static class Builder {
        private String name;
        private String email;
        private List<String> subscribedCities = new ArrayList<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder subscribeTo(String city) {
            this.subscribedCities.add(city);
            return this;
        }

        public User build() {
            if (name == null || email == null) {
                throw new IllegalStateException("Name and email are required");
            }
            return new User(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}








