package com.example.project.service;

import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSenderService mailSenderService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByLogin(username);
    }

    public User loadUserById(Long id) {
        User userFromDb = userRepo.findUserById(id);
        return userFromDb;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByLogin(user.getLogin());
        if (userFromDb != null) {
            return false;
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(user);

        sendMessageToEmail(user);
        return true;
    }

    private void sendMessageToEmail(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to unhinged. Please visit next link: http://localhost:8080/activate/%s",
                    user.getLogin(),
                    user.getActivationCode()
            );
            mailSenderService.send(user.getEmail(), "Activation code for unhinged", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActive(true);
        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }

    public Iterable<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public void updateUser(User user, User newUser) {
        boolean isEmailChanged = (!user.getEmail().equals(newUser.getEmail()) && newUser.getEmail() != null);
        if (isEmailChanged) {
            if (!StringUtils.isEmpty(newUser.getEmail())) {
                newUser.setActivationCode(UUID.randomUUID().toString());
                sendMessageToEmail(newUser);
            }
        }
        userRepo.save(newUser);
    }
}
