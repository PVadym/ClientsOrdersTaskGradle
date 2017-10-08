package ua.com.cashup.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.cashup.application.entity.User;
import ua.com.cashup.application.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Вадим on 06.10.2017.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(String username) {

        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NullPointerException("User with name " + username + " is not exist in database.");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = findByUsername(username);
        } catch (NullPointerException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),roles);
    }
}
