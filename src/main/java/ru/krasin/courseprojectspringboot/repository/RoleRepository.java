package ru.krasin.courseprojectspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.krasin.courseprojectspringboot.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
