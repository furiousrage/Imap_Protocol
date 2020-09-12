package com.example.demo.dto;

import lombok.Data;

@Data
public class EmailDTO {

    private String from;
    private String subject;
    private String body;
    private String date;

}
