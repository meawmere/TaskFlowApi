package dev.meawmere.taskflow.payload;

import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
public class TaskCreateResponse {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime createdAt;
}