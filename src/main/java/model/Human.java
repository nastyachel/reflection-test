package model;

import annotations.CustomDateFormat;
import annotations.JsonValue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by nastya on 24.04.16.
 */
public class Human {

    private String firstName;

    private String lastName;

    private int age;

    private double salary;

    private boolean isMarried;

    @JsonValue(name = "fun")
    private String hobby;

    private Collection children;

    @CustomDateFormat(format = "dd-MM-yyyy")
    private LocalDate birthDate;

    public Human(String name, String lastName){
        this.firstName = name;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void setMarried(boolean married) {
        isMarried = married;
    }

    public Collection getСhildren() {
        return children;
    }

    public void setСhildren(Collection сhildren) {
        this.children = сhildren;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
