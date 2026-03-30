package com.pao.laboratory03.exercise;

import com.pao.laboratory03.exercise.Student;
import com.pao.laboratory03.exercise.Subject;
import com.pao.laboratory03.exercise.InvalidGradeException;
import com.pao.laboratory03.exceptions.InvalidAgeException;

import java.util.*;
import java.util.stream.Collectors;

public class StudentService {

    private static StudentService instance;

    private final List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul " + name + " există deja!");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        return students.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("Studentul " + name + " nu a fost găsit!"));
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student s = findByName(studentName);
        s.addGrade(subject, grade);
    }

    public void printAllStudents() {
        for (Student s : students) {
            System.out.println(s);
        }
    }

    public void printTopStudents() {
        students.stream()
                .sorted((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()))
                .forEach(s -> System.out.println(s.getName() + " - media: " + s.getAverage()));
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, List<Double>> temp = new HashMap<>();

        for (Student s : students) {
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                temp.putIfAbsent(entry.getKey(), new ArrayList<>());
                temp.get(entry.getKey()).add(entry.getValue());
            }
        }

        Map<Subject, Double> averages = new HashMap<>();
        for (Map.Entry<Subject, List<Double>> entry : temp.entrySet()) {
            double sum = entry.getValue().stream().mapToDouble(Double::doubleValue).sum();
            double avg = sum / entry.getValue().size();
            averages.put(entry.getKey(), avg);
        }
        return averages;
    }
}