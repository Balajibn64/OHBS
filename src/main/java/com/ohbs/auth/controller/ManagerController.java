package com.ohbs.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("/profile")
    public ResponseEntity<String> getUserData() {
        return ResponseEntity.ok("Only Manager can see this");
    }
}


