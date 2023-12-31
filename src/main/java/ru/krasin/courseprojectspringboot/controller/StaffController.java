package ru.krasin.courseprojectspringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.krasin.courseprojectspringboot.entity.Staff;
import ru.krasin.courseprojectspringboot.entity.UserAction;
import ru.krasin.courseprojectspringboot.repository.StaffRepository;
import ru.krasin.courseprojectspringboot.repository.UserActionRepository;
import ru.krasin.courseprojectspringboot.service.UserActionService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class StaffController {
    private final StaffRepository staffRepository;
    private final UserActionService userActionService;
    private final UserActionRepository userActionRepository;
    @Autowired
    public StaffController(StaffRepository staffRepository, UserActionService userActionService, UserActionRepository userActionRepository) {
        this.staffRepository = staffRepository;
        this.userActionService = userActionService;
        this.userActionRepository = userActionRepository;
    }
    @GetMapping("/staff")
    public ModelAndView getAllStaff() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Получение email текущего аутентифицированного пользователя
        System.out.println("Received userEmail: " + email);

        ModelAndView mav = new ModelAndView("list-staff");

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
            // Если текущий пользователь имеет роль ROLE_USER, отображаем только его записи
            mav.addObject("staff", staffRepository.findByUserEmail(email));
        } else {
            // Если у пользователя другая роль, показываем все записи
            mav.addObject("staff", staffRepository.findAll());
        }
        String userEmail = authentication.getName();
        userActionService.logUserAction(userEmail, "Reading form Staff");
        return mav;
    }
    @GetMapping("/addStaffForm")
    public ModelAndView addStaffForm() {
        ModelAndView mav = new ModelAndView("add-staff-form");
        Staff staff = new Staff();
        mav.addObject("staff", staff);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userActionService.logUserAction(userEmail, "Reading form");
        return mav;
    }
    @PostMapping("/saveStaff")
    public RedirectView saveStaff(@ModelAttribute Staff staff) {
        // Вычислить производительность
        if (staff.getAmountOfDays() != 0) {
            double productivity = (double) staff.getAmountOfWork() / staff.getAmountOfDays();
            double roundedProductivity = Math.round(productivity * 10.0) / 10.0; // Округляем до десятой доли
            staff.setProductivity(roundedProductivity);
        }
        // Получение email текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // предполагается, что email пользователя хранится в имени пользователя

        // Установка userEmail для сотрудника
        staff.setUserEmail(userEmail);

        // Сохранение staff с указанием email пользователя
        Staff saveStaff = staffRepository.save(staff);
        userActionService.logUserAction(userEmail, "Added new staff");
        return new RedirectView("staff");
    }
    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long staffId) {
        ModelAndView mav = new ModelAndView("add-staff-form");
        Optional<Staff> optionalStaff = staffRepository.findById(staffId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Staff staff = new Staff();
        if (optionalStaff.isPresent()) {
            staff = optionalStaff.get();
        }
        mav.addObject("staff", staff);
        userActionService.logUserAction(userEmail, "Update Form");
        return mav;
    }
    @GetMapping("/deleteStaff")
    public RedirectView deleteStaff(@RequestParam Long staffId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userActionService.logUserAction(userEmail, "Delete Staff");
        staffRepository.deleteById(staffId);
        return new RedirectView("staff");
    }

    // Метод для страницы Logging
    @GetMapping("/logging")
    public String getLoggingPage(Model model) {
        List<UserAction> userActions = userActionRepository.findAll(); // Получение всех действий из базы
        model.addAttribute("userActions", userActions); // Передача действий в шаблон Thymeleaf
        return "logging";
    }
        @GetMapping("/about")
        public String aboutPage() {
            return "about";
        }

}