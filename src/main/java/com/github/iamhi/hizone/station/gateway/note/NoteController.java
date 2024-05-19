package com.github.iamhi.hizone.station.gateway.note;

import com.github.iamhi.hizone.station.core.note.NoteDto;
import com.github.iamhi.hizone.station.core.note.NoteService;
import com.github.iamhi.hizone.station.gateway.note.requests.CreateNoteRequest;
import com.github.iamhi.hizone.station.gateway.note.requests.EditNoteRequest;
import com.github.iamhi.hizone.station.gateway.note.responses.NoteResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/note")
public record NoteController(
    NoteService noteService
) {

    @GetMapping
    public List<NoteResponse> getNotes() {
        return noteService.getNotes().stream().map(this::toNoteResponse).collect(Collectors.toList());
    }

    @GetMapping("/{uuid}")
    public Optional<NoteResponse> readNote(@PathVariable String uuid) {
        return noteService.readNote(uuid).map(this::toNoteResponse);
    }

    @PostMapping
    public Optional<NoteResponse> createNote(@RequestBody CreateNoteRequest request) {
        return noteService.createNote(
            request.title(),
            request.content()
        ).map(this::toNoteResponse);
    }

    @PutMapping("/{uuid}")
    public Optional<NoteResponse> editNote(@PathVariable String uuid, @RequestBody EditNoteRequest request) {
        return noteService.updateNote(
            uuid,
            request.title(),
            request.content()
        ).map(this::toNoteResponse);
    }

    @DeleteMapping("/{uuid}")
    public Optional<NoteResponse> deleteNote(@PathVariable String uuid) {
        return noteService.deleteNote(uuid).map(this::toNoteResponse);

    }

    private NoteResponse toNoteResponse(NoteDto noteDto) {
        return new NoteResponse(
            noteDto.uuid(),
            noteDto.title(),
            noteDto.content()
        );
    }
}
