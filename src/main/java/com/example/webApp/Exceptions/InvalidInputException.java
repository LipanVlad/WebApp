package com.example.webApp.Exceptions;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends AppException {
    public InvalidInputException(String errorMessage){
        super(errorMessage);
    }
    @Override
    public HttpStatus getHttpStatus(){
        return HttpStatus.BAD_REQUEST;
    }
}
