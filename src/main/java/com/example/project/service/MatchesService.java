package com.example.project.service;

import com.example.project.domain.Matches;
import com.example.project.repos.MatchesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchesService {
    @Autowired
    private MatchesRepo matchesRepo;

    public boolean addLike(Matches matches) {
       Matches matchesFromDb = matchesRepo.findMatchesByWhoAndWhom(matches.getWho(), matches.getWhom());
        if (matchesFromDb != null) {
            return false;
        }
        matchesRepo.save(matches);
        return true;
    }
    public boolean doWeMatch(Matches matches){
        Matches didhelikedmeback = matchesRepo.findMatchesByWhoAndWhom(matches.getWhom(), matches.getWho());
        if(didhelikedmeback != null){
            return true;
        }
        return false;
    }
    public List<Matches> whoLikedMe(Long id){
        return matchesRepo.findByWhom(id);
    }

}
