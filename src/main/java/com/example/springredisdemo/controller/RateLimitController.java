package com.example.springredisdemo.controller;

import com.example.springredisdemo.dto.Request;
import com.example.springredisdemo.service.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RateLimitController {

    @Autowired
    private RateLimitService rateLimitService;

    @PostMapping("/process")
    public ResponseEntity<String> processRequest(@RequestBody Request descriptors) {
        if (descriptors.getDescriptors()!=null && rateLimitService.allowRequest(descriptors.getDescriptors())) {
            // Process the request
            return ResponseEntity.ok("Request processed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }
}