package com.example.webApp.DataTransferObjects;

import jakarta.validation.constraints.NotBlank;

public class CommentRequestDTO {
    @NotBlank(message = "Body cannot be empty")
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
