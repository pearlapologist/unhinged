package com.example.project.controller;

import com.example.project.domain.Photo;
import com.example.project.domain.User;
import com.example.project.repos.PhotoRepo;
import com.example.project.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.IntStream;

@Controller
public class MainController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PhotoRepo photoRepo;

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "")String filter,
                       Model model, @ModelAttribute("user") User user) {
        Iterable<User> users;

        if (filter != null && !filter.isEmpty()) {
            users = userRepo.findByCity(filter);
        } else {
            users = userRepo.findAll();
        }
        Iterable<Photo> photos = photoRepo.findAll();
        model.addAttribute("users", users);
        model.addAttribute("photos", photos);
        model.addAttribute("filter", filter);
        return "main";
    }

    @GetMapping("/likes")
    public String likes (@RequestParam(required = false, defaultValue = "")String filter,
                       Model model, @ModelAttribute("user") User user) {
        Iterable<User> users;

        if (filter != null && !filter.isEmpty()) {
            users = userRepo.findByCity(filter);
        } else {
            users = userRepo.findAll();
        }
        Iterable<Photo> photos = photoRepo.findAll();
        model.addAttribute("users", users);
        model.addAttribute("photos", photos);
        model.addAttribute("filter", filter);
        return "likes";
    }

    @GetMapping("/master")
    public String master(Model model, @RequestParam(value = "page", required = false, defaultValue = "0")Integer page) {
        Page<User> pageUsers = userRepo.findAll(PageRequest.of(page,1, Sort.Direction.ASC,"id"));
        model.addAttribute("usersPage", pageUsers);
        model.addAttribute("currentPage", page);
        model.addAttribute("numbers", IntStream.range(0,pageUsers.getTotalPages()).toArray());
        return "master";
    }
}
