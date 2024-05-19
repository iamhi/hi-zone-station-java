package com.github.iamhi.hizone.station.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends CrudRepository<NoteEntity, Integer> {

    List<NoteEntity> findByOwnerUuid(String ownerUuid);

    Optional<NoteEntity> findByUuid(String uuid);

    Optional<NoteEntity> findByUuidAndOwnerUuid(String uuid, String ownerUuid);
}
