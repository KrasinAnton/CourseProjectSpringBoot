package ru.krasin.courseprojectspringboot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STAFF")
public class Staff {

    private int amountOfDays;
    private int amountOfWork;
    private double productivity;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "appointment")
    private String appointment;

    @Column(name = "phonenumber", columnDefinition = "DECIMAL(20)")
    private BigInteger phonenumber;

    @Column(name = "email")
    private String email;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Column(name = "user_email")
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public int getAmountOfDays() {
        return amountOfDays;
    }
    public void setAmountOfDays(int amountOfDays) {
        this.amountOfDays = amountOfDays;
    }
    public int getAmountOfWork() {
        return amountOfWork;
    }
    public void setAmountOfWork(int amountOfWork) {
        this.amountOfWork = amountOfWork;
    }
    public double getProductivity() {
        return productivity;
    }
    public void setProductivity(double productivity) {
        this.productivity = productivity;
    }
}

