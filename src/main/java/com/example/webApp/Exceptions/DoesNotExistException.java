package com.example.webApp.Exceptions;

import org.springframework.http.HttpStatus;

public class DoesNotExistException extends AppException{
    public DoesNotExistException(String message){
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.GONE;
    }
}
