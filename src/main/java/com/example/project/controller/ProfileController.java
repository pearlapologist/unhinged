package com.example.project.controller;

import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.repos.PhotoRepo;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/myProfile")
public class ProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private PhotoRepo photoRepo;
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String getProfile(Model model, @AuthenticationPrincipal User principal){
        User user = userService.loadUserById(principal.getId());
        model.addAttribute("user", user);
        return "myProfile";
    }

    @GetMapping("/edit")
    public String userEditForm(@AuthenticationPrincipal User principal, Model model){
        User user = userService.loadUserById(principal.getId());
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "editMyProfile";
    }

    @PostMapping("/edit")
    public String updateProfile(@AuthenticationPrincipal User principal, @ModelAttribute("user") User newUser){
        User user = userService.loadUserById(principal.getId());
        userService.updateUser(user, newUser);
        return "redirect:/myProfile";
    }

}
