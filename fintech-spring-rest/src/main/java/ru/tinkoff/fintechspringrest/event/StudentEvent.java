package ru.tinkoff.fintechspringrest.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEvent {
    private UUID id;

    private long studentId;

    private Status status;
}
