package com.github.iamhi.hizone.station.gateway.user;

import com.github.iamhi.hizone.station.config.ExtendedUserDetails;
import com.github.iamhi.hizone.station.core.user.UserDto;
import com.github.iamhi.hizone.station.core.user.UserService;
import com.github.iamhi.hizone.station.gateway.user.requests.AddRoleRequest;
import com.github.iamhi.hizone.station.gateway.user.requests.SignUpRequest;
import com.github.iamhi.hizone.station.gateway.user.responses.UserResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/user")
@RestController
public record UserController(
    UserService userService
) {

    @PostMapping("/signup")
    public UserResponse signup(SignUpRequest signUpRequest) {
        UserDto userDto = userService.createUser(
            signUpRequest.username(),
            signUpRequest.password(),
            signUpRequest.email()
        );

        return new UserResponse(
            userDto.uuid(),
            userDto.username(),
            userDto.email());
    }

    @PostMapping("/role")
    public Optional<UserResponse> addRoles(@RequestBody AddRoleRequest request) {
        return userService.addRole(request.role(),
            request.rolePassword()).map(this::mapToResponse);
    }

    @GetMapping("/me")
    public Optional<UserResponse> me() {
        ExtendedUserDetails principal = (ExtendedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService.findUserByUuid(principal.getUuid()).map(this::mapToResponse);
    }

    private UserResponse mapToResponse(UserDto userDto) {
        return new UserResponse(
            userDto.uuid(),
            userDto.username(),
            userDto.email());
    }
}
