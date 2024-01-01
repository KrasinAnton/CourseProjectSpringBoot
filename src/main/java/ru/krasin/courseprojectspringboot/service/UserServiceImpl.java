package ru.krasin.courseprojectspringboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.krasin.courseprojectspringboot.dto.UserDto;
import ru.krasin.courseprojectspringboot.entity.Role;
import ru.krasin.courseprojectspringboot.entity.User;
import ru.krasin.courseprojectspringboot.repository.RoleRepository;
import ru.krasin.courseprojectspringboot.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role defaultRole = roleRepository.findByName("READ_ONLY");
        if (defaultRole == null) {
            defaultRole = new Role("READ_ONLY");
            roleRepository.save(defaultRole);
        }

        user.setRoles(Collections.singletonList(defaultRole));
        userRepository.save(user);

        logger.info("New user registered with role: " + defaultRole.getName());
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        userDto.setRoles(roles);
        return userDto;
    }


}

