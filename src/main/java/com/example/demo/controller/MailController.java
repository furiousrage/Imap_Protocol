package com.example.demo.controller;

import com.example.demo.dto.EmailDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.IReadingEmail;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class MailController {

   @Autowired
    private IReadingEmail readingEmail;

   @PostMapping("/emailData")
    public List<EmailDTO> fetchedEmail(@RequestBody UserDTO userDTO)  {

       return readingEmail.readMails(userDTO);

   }

}
