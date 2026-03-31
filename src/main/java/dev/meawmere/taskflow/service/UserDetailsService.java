package dev.meawmere.taskflow.service;

import dev.meawmere.taskflow.model.UserAccount;
import dev.meawmere.taskflow.repository.UserRepository;
import dev.meawmere.taskflow.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
               String.format("User '%s' not found", username)
        ));

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("USER")
        );

        UserDetails userDetails = UserDetailsImpl.build(user, authorities);

        System.out.println("Loaded user: " + username + ", authorities: " + userDetails.getAuthorities());

        return userDetails;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
