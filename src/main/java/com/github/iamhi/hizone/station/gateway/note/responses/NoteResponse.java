package com.github.iamhi.hizone.station.gateway.note.responses;

public record NoteResponse(
    String uuid,
    String title,
    String content
) {
}