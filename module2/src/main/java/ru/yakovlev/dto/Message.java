package ru.yakovlev.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {
    private Long userId;
    private Long recipientId;
    private String message;
    private Timestamp timestamp;
}
