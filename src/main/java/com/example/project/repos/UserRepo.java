package com.example.project.repos;

import com.example.project.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<User, Long> {
    List<User> findByCity(String city);

    User findByLogin(String login);
    User findUserById(Long id);

    User findByActivationCode(String code);

    Page<User> findAll(Pageable pageable);
}
