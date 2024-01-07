package ru.krasin.courseprojectspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.krasin.courseprojectspringboot.entity.UserAction;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {


}

