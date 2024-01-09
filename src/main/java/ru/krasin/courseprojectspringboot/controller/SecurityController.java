package ru.krasin.courseprojectspringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.krasin.courseprojectspringboot.dto.UserDto;
import ru.krasin.courseprojectspringboot.entity.User;
import ru.krasin.courseprojectspringboot.service.UserService;
import java.util.List;

@Slf4j
@Controller
public class SecurityController {
    private final UserService userService;
    public SecurityController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/index")
    public String home() {
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }
    @PostMapping("/register/save")
    public String registration(@Validated @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "На этот адрес электронной почты уже зарегистрирована учетная запись");
        }

        if (result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }
        userService.saveUser(userDto);
        return "redirect:/register?success";
    }
    @GetMapping("/users")
    public String users(Model model){
        List<Object> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
    @GetMapping("/addAdminRole")
    public String addAdminRole(@RequestParam String userEmail) {
        userService.addAdminRoleToUser(userEmail);
        return "redirect:/users";
    }
    @GetMapping("/addReadRole")
    public String addReadRole(@RequestParam String userEmail) {
        userService.addReadRoleToUser(userEmail);
        return "redirect:/users";
    }
    @GetMapping("/addUserRole")
    public String addUserRole(@RequestParam String userEmail) {
        userService.addUserRoleToUser(userEmail);
        return "redirect:/users";
    }
    @GetMapping("/addRole")
    public String addRoleToUser(@RequestParam String userEmail, @RequestParam String roleName) {
        switch (roleName) {
            case "ROLE_ADMIN" -> userService.toggleAdminRole(userEmail);
            case "ROLE_READ" -> userService.toggleReadRole(userEmail);
            case "ROLE_USER" -> userService.toggleUserRole(userEmail);
        }
        return "redirect:/users";
    }

}



