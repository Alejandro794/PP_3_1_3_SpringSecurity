package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository repository, @Lazy PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }


    public List<User> getUsersList() {
        return repository.findAll();
    }


    public User getUser(Long id) {
        Optional<User> user = repository.findById(id);
        return user.orElse(null);
    }


    public void createNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }


    public void updateUser(Long id, User user) {
        user.setId(id);
        repository.save(user);
    }


    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}