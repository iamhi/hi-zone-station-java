package com.github.iamhi.hizone.station.core.user;

import com.github.iamhi.hizone.station.config.ExtendedUserDetails;
import com.github.iamhi.hizone.station.config.UserRoleConfig;
import com.github.iamhi.hizone.station.data.UserEntity;
import com.github.iamhi.hizone.station.data.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public record UserServiceImpl(
    UserRepository userRepository,
    BCryptPasswordEncoder bCryptPasswordEncoder,
    UserRoleConfig userRoleConfig
) implements UserService {

    @Override
    public UserDto createUser(String username, String password, String email) {
        UserEntity userEntity = generateEntity();

        userEntity.setUsername(username);
        userEntity.setPassword(bCryptPasswordEncoder.encode(password));
        userEntity.setEmail(email);

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return mapToDto(savedUserEntity);
    }

    @Override
    public Optional<UserDto> findUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid).map(this::mapToDto);
    }

    @Override
    public Optional<UserDto> addRole(String role, String rolePassword) {
        if (validateRole(role, rolePassword)) {
            ExtendedUserDetails principal = (ExtendedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return userRepository.findByUuid(principal.getUuid())
                .map(userEntity -> {
                    userEntity.setRoles(Stream.concat(Arrays.stream(userEntity.getRoles().split(",")),
                        Stream.of(role)).distinct().collect(Collectors.joining(",")));

                    return userRepository.save(userEntity);
                }).map(this::mapToDto);
        }

        return Optional.empty();
    }

    @Override
    public String getUserUuid(String username, String password) {
        return userRepository.findByUsername(username)
            .filter(userEntity -> bCryptPasswordEncoder.matches(password, userEntity.getPassword()))
            .map(UserEntity::getUuid).orElse(StringUtils.EMPTY);
    }

    private boolean validateRole(String role, String rolePassword) {
        return StringUtils.equals(userRoleConfig.getRoles().get(role), rolePassword);
    }

    private UserEntity generateEntity() {
        UserEntity userEntity = new UserEntity();

        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setCreatedAt(Instant.now());
        userEntity.setUpdatedAt(Instant.now());
        userEntity.setRoles("ROLE_BASIC");

        return userEntity;
    }

    private UserDto mapToDto(UserEntity userEntity) {
        return new UserDto(
            userEntity.getUuid(),
            userEntity.getUsername(),
            userEntity.getEmail(),
            Arrays.stream(userEntity.getRoles().split(";")).toList(),
            userEntity.getCreatedAt(),
            userEntity.getUpdatedAt()
        );
    }
}
