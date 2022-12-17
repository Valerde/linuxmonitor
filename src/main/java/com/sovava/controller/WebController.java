package com.sovava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/monitors")
    public String getPage() {
        return "monitor";
    }
}
