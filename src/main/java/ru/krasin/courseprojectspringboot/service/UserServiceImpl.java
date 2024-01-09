package ru.krasin.courseprojectspringboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.krasin.courseprojectspringboot.dto.UserDto;
import ru.krasin.courseprojectspringboot.entity.Role;
import ru.krasin.courseprojectspringboot.entity.User;
import ru.krasin.courseprojectspringboot.repository.RoleRepository;
import ru.krasin.courseprojectspringboot.repository.UserRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserActionService userActionService;
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           UserActionService userActionService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userActionService = userActionService;
    }
    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        List<Role> allRoles = roleRepository.findAll();

        if (allRoles.isEmpty()) {
            Role roleUser = new Role();
            roleUser.setName("ROLE_USER");
            roleRepository.save(roleUser);

            Role roleRead = new Role();
            roleRead.setName("ROLE_READ");
            roleRepository.save(roleRead);

            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");
            roleRepository.save(roleAdmin);

            allRoles = Arrays.asList(roleUser, roleRead, roleAdmin);
        }

        if (userRepository.count() == 0) {
            user.setRoles(Collections.singletonList(allRoles.get(2))); // Первый пользователь - ADMIN
        } else {
            user.setRoles(Collections.singletonList(allRoles.get(1))); // Остальные пользователи - READ
        }
        userRepository.save(user);
        userActionService.logUserAction(userDto.getEmail(), "User saved");
    }
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public List<Object> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }
    @Override
    public void addAdminRoleToUser(@RequestParam String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        if (user != null && roleAdmin != null && !user.getRoles().contains(roleAdmin)) {
            user.getRoles().add(roleAdmin);
            userRepository.save(user);
            userActionService.logUserAction(userEmail, "Add Admin role");
        }
    }
    @Override
    public void addReadRoleToUser(@RequestParam String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        Role roleRead = roleRepository.findByName("ROLE_READ");
        if (user != null && roleRead != null && !user.getRoles().contains(roleRead)) {
            user.getRoles().add(roleRead);
            userRepository.save(user);
            userActionService.logUserAction(userEmail, "Add Read role");
        }
    }
    @Override
    public void addUserRoleToUser(@RequestParam String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        Role roleUser = roleRepository.findByName("ROLE_USER");
        if (user != null && roleUser != null && !user.getRoles().contains(roleUser)) {
            user.getRoles().add(roleUser);
            userRepository.save(user);
            userActionService.logUserAction(userEmail, "Add User role");
        }
    }
    @Override
    public void toggleAdminRole(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        userActionService.logUserAction(userEmail, "Toggled Admin role");
        if (user != null && roleAdmin != null) {
            if (user.getRoles().contains(roleAdmin)) {
                user.getRoles().remove(roleAdmin);
            } else {user.getRoles().add(roleAdmin);
            }
            userRepository.save(user);
        }

    }
    @Override
    public void toggleReadRole(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        Role roleRead = roleRepository.findByName("ROLE_READ");
        userActionService.logUserAction(userEmail, "Toggled Read role");
        if (user != null && roleRead != null) {
            if (user.getRoles().contains(roleRead)) {
                user.getRoles().remove(roleRead);
            } else {user.getRoles().add(roleRead);
            }
            userRepository.save(user);
        }

    }
    @Override
    public void toggleUserRole(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        Role roleUser = roleRepository.findByName("ROLE_USER");
        userActionService.logUserAction(userEmail, "Toggled User role");
        if (user != null && roleUser != null) {
            if (user.getRoles().contains(roleUser)) {
                user.getRoles().remove(roleUser);
            } else {user.getRoles().add(roleUser);
            }
            userRepository.save(user);
        }
    }
    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        if (str.length >= 2) {
            userDto.setFirstName(str[0]);
            userDto.setLastName(str[1]);
        } else if (str.length == 1) {
            userDto.setFirstName(str[0]);
            userDto.setLastName(user.getLastName());
        }
        userDto.setEmail(user.getEmail());

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        userDto.setRoles(roles);
        return userDto;
    }
}