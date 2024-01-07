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

    @Column(name = "email") // Добавляем поле для email
    private String email;

    // Геттер для поля email
    public String getEmail() {
        return email;
    }

    // Сеттер для поля email
    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "user_email") // New field for user email
    private String userEmail;

    // Геттер для поля userEmail
    public String getUserEmail() {
        return userEmail;
    }

    // Сеттер для поля userEmail
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }



}
