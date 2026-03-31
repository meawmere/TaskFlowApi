package dev.meawmere.taskflow.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskUpdateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
}
