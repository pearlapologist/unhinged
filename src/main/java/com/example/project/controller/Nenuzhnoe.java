package com.example.project.controller;

import com.example.project.domain.Photo;
import com.example.project.domain.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public class Nenuzhnoe {
/*    @GetMapping("/main")
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
    }*/
}
