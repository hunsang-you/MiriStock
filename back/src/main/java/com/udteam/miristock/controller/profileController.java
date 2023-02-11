package com.udteam.miristock.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/profilee")
@RestController
public class profileController {
    @Value("${spring.profiles.active}")
    private String profile;

    @GetMapping
    public ResponseEntity<String> getProfile(){
        return ResponseEntity.ok().body(profile);
    }
}
