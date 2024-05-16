package com.github.iamhi.hizone.station.core.user;

public class UserNotFoundException extends RuntimeException {

    UserNotFoundException(String uuid) {
        super("User not found with uuid: " + uuid);
    }
}
