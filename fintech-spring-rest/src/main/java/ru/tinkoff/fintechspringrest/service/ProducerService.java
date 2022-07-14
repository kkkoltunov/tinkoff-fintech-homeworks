package ru.tinkoff.fintechspringrest.service;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import ru.tinkoff.fintechspringrest.event.StudentEvent;

import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class ProducerService {

    private final KafkaTemplate<String, StudentEvent> kafkaTemplate;

    private final String TOPIC = "000m6ke2-fintech-out";

    public boolean sendStudentEvent(StudentEvent studentEvent) throws ExecutionException, InterruptedException {
        ListenableFuture<SendResult<String, StudentEvent>> future = kafkaTemplate.send(TOPIC, studentEvent);
        return future.get().getRecordMetadata().offset() >= 0;
    }
}

