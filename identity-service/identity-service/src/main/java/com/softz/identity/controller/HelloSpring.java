package com.softz.identity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloSpring {

    @GetMapping("hello")
    String hello() {
        return "Hello Spring boot";
    }
}
