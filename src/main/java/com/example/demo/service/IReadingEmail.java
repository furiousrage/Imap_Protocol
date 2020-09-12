package com.example.demo.service;

import com.example.demo.dto.EmailDTO;
import com.example.demo.dto.UserDTO;

import javax.mail.Message;
import java.util.List;


public interface IReadingEmail {

    List<EmailDTO> readMails(UserDTO userDTO);
}
