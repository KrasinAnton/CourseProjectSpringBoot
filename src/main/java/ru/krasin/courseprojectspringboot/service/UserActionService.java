package ru.krasin.courseprojectspringboot.service;

import org.springframework.stereotype.Service;
import ru.krasin.courseprojectspringboot.entity.UserAction;
import ru.krasin.courseprojectspringboot.repository.UserActionRepository;

import java.time.LocalDateTime;

@Service
public class UserActionService {
    private final UserActionRepository userActionRepository;

    public UserActionService(UserActionRepository userActionRepository) {
        this.userActionRepository = userActionRepository;
    }

    public void logUserAction(String userEmail, String action) {
        System.out.println("Received userEmail: " + userEmail);
        UserAction userAction = new UserAction();
        userAction.setUserEmail(userEmail);
        userAction.setAction(action);
        userAction.setTimestamp(LocalDateTime.now());

        userActionRepository.save(userAction);
    }
}

