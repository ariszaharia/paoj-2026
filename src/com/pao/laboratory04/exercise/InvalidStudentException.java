package com.pao.laboratory03.exercise;

import com.pao.laboratory03.exceptions.InvalidAgeException;

public class InvalidStudentException extends RuntimeException{
    public InvalidStudentException(String message){
        super(message);
    }
}
