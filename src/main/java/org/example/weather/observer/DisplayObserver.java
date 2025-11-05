package org.example.weather.observer;

import org.example.weather.model.WeatherData;
import org.example.weather.user.User;
import org.example.weather.service.NotificationHistory;
import org.example.weather.model.Notification;
import org.example.weather.user.NotificationType;
import java.time.LocalDateTime;

public class PhoneObserver implements Observer {
    private final NotificationHistory notificationHistory;

    public PhoneObserver(NotificationHistory history) {
        this.notificationHistory = history;
    }

    @Override
    public void update(User user, WeatherData data) {
        String msg = user.getName() + "'s phone received update: " + data;
        String date = LocalDateTime.now().toString();
        Notification notification = new Notification(msg, date);

        // Используй тип уведомления, например PHONE (или перебор, как выше)
        notificationHistory.log(user.getName(), NotificationType.PHONE, notification);
    }
}



