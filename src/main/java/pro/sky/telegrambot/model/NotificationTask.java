package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "notification_task")

public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "notification")
    private String notification;

    @Column(name = "alarm_date")
    private LocalDateTime alarmDate;

    public NotificationTask() {
    }

    public NotificationTask(Long chatId, String notification, LocalDateTime alarmDate) {
        this.chatId = chatId;
        this.notification = notification;
        this.alarmDate = alarmDate;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getNotification() {
        return notification;
    }

    public LocalDateTime getAlarmDate() {
        return alarmDate;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public void setAlarmDate(LocalDateTime alarmDate) {
        this.alarmDate = alarmDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return notification;
    }
}

