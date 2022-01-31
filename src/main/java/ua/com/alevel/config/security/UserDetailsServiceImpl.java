package ua.com.alevel.config.security;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.alevel.persistence.repository.user.UserRepository;
import ua.com.alevel.persistence.type.Status;
import ua.com.alevel.persistence.entity.user.User;

import static ua.com.alevel.util.Validation.emailValidate;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if(emailValidate(username)){
            user = userRepository.findByEmail(username);
        }else{
            user = userRepository.findByLogin(username);
        }
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("invalid username or password");
        }
        return fromUser(user);
    }

    public static UserDetails fromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                user.isEnabled(),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getRole().getAuthorities()
        );
    }
}