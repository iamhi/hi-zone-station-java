package com.github.iamhi.hizone.station.data.wellbeing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface WeightLogRepository extends JpaRepository<WeightLogEntity, Integer> {

    String APPLICATION_NAME = "weight-log";

    List<WeightLogEntity> findByOwnerUuid(String ownerUuid);

    List<WeightLogEntity> findByOwnerUuidOrderByCreatedAtDesc(String ownerUuid);

    Optional<WeightLogEntity> findByUuidAndOwnerUuid(String uuid, String ownerUuid);
}
