package com.example.springredisdemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateLimitRule {
    private String accountId;
    private String clientIp;
    private String requestType;
    private int allowedNumberOfRequests;
    private TimeInterval timeInterval;
//    private String timeInterval;

    // Getters and setters
}