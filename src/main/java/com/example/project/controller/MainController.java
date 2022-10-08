package com.example.project.controller;

import com.example.project.domain.Matches;
import com.example.project.domain.Photo;
import com.example.project.domain.Redflag;
import com.example.project.domain.User;
import com.example.project.repos.MatchesRepo;
import com.example.project.repos.PhotoRepo;
import com.example.project.repos.RedflagRepo;
import com.example.project.repos.UserRepo;
import com.example.project.service.MatchesService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
public class MainController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private PhotoRepo photoRepo;
    @Autowired
    private MatchesRepo matchesRepo;
    @Autowired
    private RedflagRepo redflagRepo;
    @Autowired
    private MatchesService matchesService;

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                       @AuthenticationPrincipal User principal,
                       Model model) {
        Page<User> pageUsers = userRepo.findAll(PageRequest.of(page, 1, Sort.Direction.ASC, "id"));

        List<Redflag> redflagList = redflagRepo.findRedflagsByUserid(principal.getId());
        model.addAttribute("redflags", redflagList);
        model.addAttribute("usersPage", pageUsers);
        model.addAttribute("currentPage", page);
        return "main";
    }

    @GetMapping("/main/{user}/like")
    public String like(
            @AuthenticationPrincipal User principal,
            @PathVariable User user,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer,
            Model model
    ) {
        Matches matches = new Matches(principal.getId(), user.getId());
        if (matchesService.doWeMatch(matches)) {
            return "profile"+"/" + user.getId();
        } else {
            if (!matchesService.addLike(matches)) {
                model.addAttribute("message", "You already liked this person");
            } else {
                matchesRepo.save(matches);
                model.addAttribute("message", "You successfully liked this person");
            }
        }
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams().entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
        return "redirect:" + components.getPath();
    }

    @GetMapping("/likes")
    public String likes(@RequestParam(required = false, defaultValue = "") String filter,
                        @AuthenticationPrincipal User principal,
                        Model model) {
        List<Matches> likes = matchesService.whoLikedMe(principal.getId());
        ArrayList<User> users = new ArrayList<>();
        for (Matches like : likes) {
            User user = userRepo.findUserById(like.getWho());
            users.add(user);
        }

        //   model.addAttribute("photos", photos);
        model.addAttribute("users", users);
        return "likes";
    }
}
