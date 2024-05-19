package com.github.iamhi.hizone.station.core.note;

import com.github.iamhi.hizone.station.core.user.MemberCache;
import com.github.iamhi.hizone.station.data.NoteEntity;
import com.github.iamhi.hizone.station.data.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
record NoteServiceImpl(
    NoteRepository noteRepository,
    MemberCache memberCache
) implements NoteService {

    static NoteDto mapEntityToDto(NoteEntity noteEntity) {
        return new NoteDto(
            noteEntity.getUuid(),
            noteEntity.getTitle(),
            noteEntity.getContent(),
            noteEntity.getCreatedAt(),
            noteEntity.getUpdatedAt()
        );
    }

    @Override
    public Optional<NoteDto> createNote(String title, String content) {
        NoteEntity noteEntity = generateEmptyEntity();

        noteEntity.setOwnerUuid(memberCache.getUuid());
        noteEntity.setTitle(title);
        noteEntity.setContent(content);

        return Optional.of(mapEntityToDto(noteRepository.save(noteEntity)));
    }

    @Override
    public List<NoteDto> getNotes() {
        return noteRepository.findByOwnerUuid(memberCache.getUuid())
            .stream()
            .map(NoteServiceImpl::mapEntityToDto)
            .toList();
    }

    @Override
    public Optional<NoteDto> readNote(String uuid) {
        return noteRepository.findByUuidAndOwnerUuid(uuid, memberCache.getUuid())
            .map(NoteServiceImpl::mapEntityToDto);
    }

    @Override
    public Optional<NoteDto> updateNote(String uuid, String title, String content) {
        Optional<NoteEntity> optionalNoteEntity = noteRepository.findByUuid(uuid);

        if (optionalNoteEntity.isPresent()) {
            NoteEntity noteEntity = optionalNoteEntity.get();

            noteEntity.setTitle(title);
            noteEntity.setContent(content);
            noteEntity.setUpdatedAt(Instant.now());

            return Optional.of(NoteServiceImpl.mapEntityToDto(noteRepository.save(noteEntity)));
        }

        return Optional.empty();
    }

    @Override
    public Optional<NoteDto> deleteNote(String uuid) {
        Optional<NoteEntity> optionalNoteEntity = noteRepository.findByUuid(uuid);

        return optionalNoteEntity.map(entity -> {
            noteRepository.delete(entity);

            return entity;
        }).map(NoteServiceImpl::mapEntityToDto);
    }

    private NoteEntity generateEmptyEntity() {
        NoteEntity noteEntity = new NoteEntity();

        noteEntity.setUuid(UUID.randomUUID().toString());
        noteEntity.setUpdatedAt(Instant.now());
        noteEntity.setCreatedAt(Instant.now());

        return noteEntity;
    }
}
