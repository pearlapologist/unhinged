package com.example.project.repos;

import com.example.project.domain.Redflag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedflagRepo extends CrudRepository<Redflag, Long> {
    List<Redflag> findRedflagsByUserid(Long id);

    Redflag findRedflagById(Long id);

}
