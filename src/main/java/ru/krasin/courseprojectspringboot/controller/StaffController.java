package ru.krasin.courseprojectspringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.krasin.courseprojectspringboot.entity.Staff;
import ru.krasin.courseprojectspringboot.repository.StaffRepository;

import java.util.Optional;

@Slf4j
@Controller
public class StaffController {
    @Autowired
    private StaffRepository staffRepository;

    @GetMapping("/list")
    public ModelAndView getAllStaff() {
        log.info("/list -> connections");
        ModelAndView mav = new ModelAndView("list-staff");
        mav.addObject("staff", staffRepository.findAll());
        return mav;
    }

    @GetMapping("/addStaffForm")
    public ModelAndView addStaffForm() {
        ModelAndView mav = new ModelAndView("add-staff-form");
        Staff staff = new Staff();
        mav.addObject("staff", staff);
        return mav;
    }
    @PostMapping("/saveStaff")
    public RedirectView saveStaff(@ModelAttribute Staff staff) {
        staffRepository.save(staff);
        return new RedirectView("list");
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long staffId) {
        ModelAndView mav = new ModelAndView("add-staff-form");
        Optional<Staff> optionalStaff = staffRepository.findById(staffId);
        Staff staff = new Staff();
        if (optionalStaff.isPresent()) {
            staff = optionalStaff.get();
        }
        mav.addObject("staff", staff);
        return mav;
    }

    @GetMapping("/deleteStaff")
    public RedirectView deleteStaff(@RequestParam Long staffId) {
        staffRepository.deleteById(staffId);
        return new RedirectView("list");
    }

}
