package com.github.iamhi.hizone.station.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends CrudRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUuid(String uuid);

    Optional<UserEntity> findByUsername(String username);
}
