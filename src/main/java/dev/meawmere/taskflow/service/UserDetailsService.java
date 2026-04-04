package dev.meawmere.taskflow.service;

import dev.meawmere.taskflow.model.UserAccount;
import dev.meawmere.taskflow.repository.UserRepository;
import dev.meawmere.taskflow.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
               String.format("User '%s' not found", username)
        ));

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("USER")
        );

        UserDetails userDetails = UserDetailsImpl.build(user, authorities);

        log.debug("Loaded user: {}, authorities: {}", username, userDetails.getAuthorities());

        return userDetails;
    }
}
