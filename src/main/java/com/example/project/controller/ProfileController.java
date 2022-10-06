package com.example.project.controller;

import com.example.project.domain.Photo;
import com.example.project.domain.Role;
import com.example.project.domain.User;
import com.example.project.repos.PhotoRepo;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
    public String updateProfile(@AuthenticationPrincipal User principal,
                                @ModelAttribute("user") @Valid User newUser,
                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "editMyProfile";
        }
        User user = userService.loadUserById(principal.getId());
        userService.updateUser(user, newUser);
        return "redirect:/myProfile";
    }

    @GetMapping("/addPhoto")
    public String addPhoto(@ModelAttribute("photo") Photo photo, Model model){
        Object principal = SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
        User user = (User)principal;
        Long userId = user.getId();
        photo.setUserId(userId);
        model.addAttribute("photo", photo);
        return "addPhoto";
    }

    @PostMapping("/savePhoto")
    public String savePhoto(@ModelAttribute("photo") Photo photo, @RequestParam("file") MultipartFile file) throws IOException {
        if(file != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuiFile = UUID.randomUUID().toString();
            String resultFileName =  uuiFile+"."+ file.getOriginalFilename();

            file.transferTo(new File( uploadPath+"/"+ resultFileName));

            photo.setFilename(resultFileName);
        }
        photoRepo.save(photo);
        return "redirect:/myProfile";
    }
}
