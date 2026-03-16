package com.pao.laboratory03.exercise;

import javax.swing.text.html.parser.TagElement;
import java.util.HashMap;
import java.util.Map;

import com.pao.laboratory03.exceptions.InvalidAgeException;
import com.pao.laboratory03.exercise.Subject;

public class Student {
    private String name;
    private int age;
    private Map<Subject, Double> grades;

    public Student(String name, int age){
        grades = new HashMap<>();
        this.name = name;
        if (age < 0 || age > 150){
            throw new InvalidStudentException(age + "nu e valid");
        }
        this.age = age;
    }

    public int getAge(){
        return this.age;
    }
    public String getName(){
        return this.name;
    }
    public Map<Subject, Double> getGrades(){
        return this.grades;
    }

    void addGrade(Subject subject, Double grade){
        if (grade < 1 || grade > 10) {
            throw new InvalidGradeException("Nota " + grade + " nu este valida (1-10)");
        }
        grades.put(subject, grade);

    }
    double getAverage(){
        int n = grades.size();
        if (n == 0){
            return 0;
        }
        double suma = 0;
        for (Double grade : grades.values()){
            suma += grade;
        }
        return (suma / n);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", avg =" + this.getAverage() +
                '}';
    }
}
