package com.github.iamhi.hizone.station.core.note;

import java.time.Instant;

public record NoteDto(
    String uuid,
    String title,
    String content,
    Instant createdAt,
    Instant updatedAt
) {
}
