package com.example.project.repos;

import com.example.project.domain.Matches;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MatchesRepo extends CrudRepository<Matches, Long> {
    List<Matches> findByWho(Long id);
    List<Matches> findByWhom(Long id);

    Matches findMatchesById(Long id);

    Matches findMatchesByWhoAndWhom(Long who, Long whom);
}
