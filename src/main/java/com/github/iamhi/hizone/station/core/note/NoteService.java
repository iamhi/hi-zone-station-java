package com.github.iamhi.hizone.station.core.note;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface NoteService {

    Optional<NoteDto> createNote(String title, String content);

    List<NoteDto> getNotes();

    Optional<NoteDto> readNote(@NotBlank String uuid);

    Optional<NoteDto> updateNote(@NotBlank String uuid, String title, String content);

    Optional<NoteDto> deleteNote(@NotBlank String uuid);
}
