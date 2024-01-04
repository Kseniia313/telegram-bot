package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private TelegramBot telegramBot;

    private final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final Pattern MESSAGE_PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private final DateTimeFormatter TASK_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");




    @Override
    public void process(Update update) {
        long chatId = update.message().chat().id();
        String messageFromUser = update.message().text();

        if (messageFromUser == null) {
            logger.info("User sent empty message");
            telegramBot.execute(new SendMessage(chatId, "Отправьте /start для начала работы"));
            return;

        } if (messageFromUser.equals("/start")) {
            sendWelcomeMessageToUser(chatId);
        }
//        Matcher matcher = MESSAGE_PATTERN.matcher(messageFromUser);
//        if (matcher.find()) {
//
//        }
    }

    private void sendWelcomeMessageToUser(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Добавьте напоминание в формате: 'dd.MM.yyyy HH:mm текст напоминания'"));
    }
}
