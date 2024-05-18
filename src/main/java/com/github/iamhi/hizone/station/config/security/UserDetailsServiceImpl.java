package com.github.iamhi.hizone.station.config.security;

import com.github.iamhi.hizone.station.config.ExtendedUserDetails;
import com.github.iamhi.hizone.station.data.UserEntity;
import com.github.iamhi.hizone.station.data.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record UserDetailsServiceImpl(
    UserRepository userRepository
) implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        UserEntity user = optionalUser.get();

        return new ExtendedUserDetails(
            user.getUuid(),
            user.getUsername(),
            user.getPassword(),
            AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles())
        );
    }
}
