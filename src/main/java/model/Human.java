package model;

import annotations.CustomDateFormat;
import annotations.JsonValue;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by nastya on 24.04.16.
 */
public class Human {

    private int age;

    private double salary;

    private boolean isMarried;

    private String firstName;

    private String lastName;

    @JsonValue(name = "fun")
    private String hobby;

    private List<Pet> pets;

    private String [] сhildren;

    @CustomDateFormat(format = "dd-MM-yyyy")
    private Date birthDate;

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

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
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

    public String[] getСhildren() {
        return сhildren;
    }

    public void setСhildren(String[] сhildren) {
        this.сhildren = сhildren;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
