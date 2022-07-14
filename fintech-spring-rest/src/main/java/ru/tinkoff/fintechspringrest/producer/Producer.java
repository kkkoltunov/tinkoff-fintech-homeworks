package ru.tinkoff.fintechspringrest.producer;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.fintechspringrest.event.StudentEvent;
import ru.tinkoff.fintechspringrest.service.ProducerService;
import ru.tinkoff.fintechspringrest.service.StudentEventService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@EnableScheduling
@AllArgsConstructor
public class Producer {

    private final ProducerService producerService;

    private final StudentEventService studentEventService;

    @Scheduled(cron = "* * * * * *")
    public void publishAllEvents() {
        List<StudentEvent> listEvents = studentEventService.findAll();
        List<UUID> uuidListSuccessfulSendEvents = new ArrayList<>();
        for (StudentEvent studentEvent : listEvents) {
            try {
                if (producerService.sendStudentEvent(studentEvent)) {
                    uuidListSuccessfulSendEvents.add(studentEvent.getId());
                }
            } catch (ExecutionException | InterruptedException e) {
                System.err.println("Exception while sending kafka messages: " + e.getMessage());
            }
        }
        studentEventService.deleteByListId(uuidListSuccessfulSendEvents);
    }
}
