package com.example.demo.dto;

import lombok.*;

@Data
public class Response {

    private int status;
    private String message;
    private Object object;

    public Response(int status, String message){
        this.message = message;
        this.status = status;
    }

    public Response(int status, String message, Object object) {
        this.message = message;
        this.status = status;
        this.object = object;
    }
}
