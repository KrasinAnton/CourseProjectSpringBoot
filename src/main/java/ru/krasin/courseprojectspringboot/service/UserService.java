package ru.krasin.courseprojectspringboot.service;

import ru.krasin.courseprojectspringboot.dto.UserDto;
import ru.krasin.courseprojectspringboot.entity.User;
import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
    List<Object> findAllUsers();

    void addAdminRoleToUser(String userEmail);
    void addReadRoleToUser(String userEmail);
    void addUserRoleToUser(String userEmail);
    void toggleAdminRole(String userEmail);
    void toggleReadRole(String userEmail);
    void toggleUserRole(String userEmail);
}

