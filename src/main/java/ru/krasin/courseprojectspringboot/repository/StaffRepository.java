package ru.krasin.courseprojectspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.krasin.courseprojectspringboot.entity.Staff;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByUserEmail(String userEmail);

}
