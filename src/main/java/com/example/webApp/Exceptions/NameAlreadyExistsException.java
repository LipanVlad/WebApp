package com.example.webApp.Exceptions;

import org.springframework.http.HttpStatus;

public class NameAlreadyExistsException extends AppException {
   public NameAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
    @Override
    public HttpStatus getHttpStatus(){
        return HttpStatus.CONFLICT;
    }
}
