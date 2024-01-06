package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service

public class NotificationServiceImpl implements NotificationService {
    private final TelegramBot telegramBot;

    private final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final Pattern MESSAGE_PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationServiceImpl(TelegramBot telegramBot,
                                   NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = telegramBot;
        this.notificationTaskRepository = notificationTaskRepository;
    }


    @Override
    public void process(Update update) {

        long chatId = update.message().chat().id();
        String messageFromUser = update.message().text();

        if (messageFromUser == null) {
            telegramBot.execute(new SendMessage(chatId, "Для начала работы отправьте /start"));
            return;
        }

        if (messageFromUser.equals("/start")) {
            logger.info("Отправка приветственного сообщения");
            sendWelcomeMessageToUser(chatId);
            return;
        }

        Matcher matcher = MESSAGE_PATTERN.matcher(messageFromUser);

        if (matcher.find()) {
            dateFormatValidation(chatId, matcher);
        } else {
            telegramBot.execute(new SendMessage(chatId, "Напоминание может быть только в формате:'dd.MM.yyyy HH:mm текст напоминания' "));
            return;
        }

        LocalDateTime alarmDate = LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMATTER);
        String notification = matcher.group(3);
        saveEntity(chatId, notification, alarmDate);
    }


    private void sendWelcomeMessageToUser(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Я напомню Вам о важных делах! Добавьте напоминание в формате: 'dd.MM.yyyy HH:mm текст напоминания'"));
    }

    private void dateFormatValidation(long chatId, Matcher matcher) {
        String dateOfNotification = matcher.group(1);
        LocalDateTime alarmDate = LocalDateTime.parse(dateOfNotification, DATE_TIME_FORMATTER);
    }

    private void saveEntity(Long chatId, String notification, LocalDateTime alarmDate) {

        if (alarmDate.isBefore(LocalDateTime.now())) {
            logger.warn("Дата напоминания не может иметь прошлое время");
            telegramBot.execute(new SendMessage(chatId, " Напоминание в прошлое отправить нельзя"));
            return;
        }

        NotificationTask notificationTask = new NotificationTask(chatId, notification, alarmDate);
        notificationTaskRepository.save(notificationTask);
        logger.info("Напоминание " + notificationTask + " сохранено");
        telegramBot.execute(new SendMessage(chatId, "Напоминание " + " ' " + notificationTask + " ' " + " сохранено "));
    }

    @Scheduled(cron = "0 0/1 * * * *")
    @Override
    public void notificationsTaskFromDataBase() {
        List<NotificationTask> tasks = notificationTaskRepository.
                findByAlarmDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        tasks.forEach(task -> {
            logger.info("Напоминание было отправлено");
            telegramBot.execute(new SendMessage(task.getChatId(), String.format("Привет! Не забудь:\n%s" + " , в %s",
                    task.getNotification(), task.getAlarmDate())));
        });
    }
}

