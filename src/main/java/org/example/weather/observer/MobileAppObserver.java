package org.example.weather.observer;

import org.example.weather.user.User;
import org.example.weather.service.NotificationHistory;
import org.example.weather.model.Notification;
import org.example.weather.user.NotificationType;
import org.example.weather.model.WeatherData;
import java.time.LocalDateTime;

public class AppObserver implements Observer {
    private final NotificationHistory notificationHistory;

    public AppObserver(NotificationHistory history) {
        this.notificationHistory = history;
    }

    @Override
    public void update(User user, WeatherData data) {
        String msg = user.getName() + " app displays: " + data;
        String date = LocalDateTime.now().toString();
        Notification notification = new Notification(msg, date);

        // Логируем по всем типам уведомлений, что есть в ENUM
        notificationHistory.log(user.getName(), NotificationType.APP, notification);
    }
}





