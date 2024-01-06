package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Update;


public interface NotificationService {
    void process(Update update);

    void notificationsTaskFromDataBase();
}
