package com.example.project.controller;

import com.example.project.domain.Photo;
import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.repos.PhotoRepo;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PhotoRepo photoRepo;
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userService.findAll());
        return "userList";
    }
    @GetMapping("{id}")
    public String userEditForm(@PathVariable Long id, Model model){
        User userFromDb = userService.loadUserById(id);

        model.addAttribute("user", userFromDb);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userSave(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "userEdit";
        }
        userService.saveUser(user);

        return "redirect:/user";
    }

}
