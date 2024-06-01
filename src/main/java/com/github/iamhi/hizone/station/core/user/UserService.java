package com.github.iamhi.hizone.station.core.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface UserService {

    int USERNAME_MAX_SIZE = 36;

    int USERNAME_MIN_SIZE = 6;

    int PASSWORD_MAX_SIZE = 255;

    int PASSWORD_MIN_SIZE = 6;

    UserDto createUser(
        @Size(min = USERNAME_MIN_SIZE, max = USERNAME_MAX_SIZE, message = "Username needs to be more then 5 character") String username,
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = "Password needs to be more then 5 character") String password,
        @Email String email
    );

    Optional<UserDto> findUserByUuid(@NotBlank String uuid);

    Optional<UserDto> addRole(@NotBlank String role, @NotBlank String password);

    String getUserUuid(@NotBlank String username, @NotBlank String password);
}
