package com.github.iamhi.hizone.station.core.note;

import java.util.List;
import java.util.Optional;

public interface NoteService {

    Optional<NoteDto> createNote(String title, String content);

    List<NoteDto> getNotes();

    Optional<NoteDto> readNote(String uuid);

    Optional<NoteDto> updateNote(String uuid, String title, String content);

    Optional<NoteDto> deleteNote(String uuid);
}
