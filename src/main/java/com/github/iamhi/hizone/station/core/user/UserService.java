package com.github.iamhi.hizone.station.core.user;

import java.util.Optional;

public interface UserService {

    UserDto createUser(
        String username,
        String password,
        String email
    );

    Optional<UserDto> findUserByUuid(String uuid);

    Optional<UserDto> addRole(String role, String password);

    String getUserUuid(String username, String password);
}
