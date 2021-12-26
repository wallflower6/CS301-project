package com.como.service.receiveapprequests;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RARWebController {
    @RequestMapping("/")
    public String getPage() {
        return "index";
    }

    @RequestMapping("/health")
    public void getHealth() {
    }

    @RequestMapping("/registration")
    public String getRegistration() {
        return "register";
    }
}
