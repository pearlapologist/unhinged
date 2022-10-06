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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
     //   model.addAttribute("existingUser", id);
        User userFromDb = userService.loadUserById(id);

        model.addAttribute("user", userFromDb);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userSave(@ModelAttribute("user") User user){
        userService.saveUser(user);

        return "redirect:/user";
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
        return "redirect:/user";
    }

}
