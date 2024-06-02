package com.github.iamhi.hizone.station.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Integer> {

    List<NoteEntity> findByOwnerUuid(String ownerUuid);

    Optional<NoteEntity> findByUuid(String uuid);

    Optional<NoteEntity> findByUuidAndOwnerUuid(String uuid, String ownerUuid);
}
