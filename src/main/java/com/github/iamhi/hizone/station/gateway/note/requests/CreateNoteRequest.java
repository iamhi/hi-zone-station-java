package com.github.iamhi.hizone.station.gateway.note.requests;

public record CreateNoteRequest(
    String title,
    String content
) {
}
