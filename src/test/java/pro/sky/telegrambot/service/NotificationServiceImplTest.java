package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationTaskRepository notificationTaskRepository;
    @InjectMocks
    private NotificationServiceImpl notificationService;
    private LocalDateTime localDateTime = LocalDateTime.now();
    private NotificationTask notificationTask = new NotificationTask(1L, "поздравить с новыи годом", localDateTime);


    @Test
    void saveEntity_shouldCreateAndSaveNotificationToRepository() {
        when(notificationTaskRepository.save(notificationTask))
                .thenReturn(notificationTask);
        NotificationTask result = notificationTaskRepository.save(notificationTask);
        assertEquals(notificationTask,result);
    }

}