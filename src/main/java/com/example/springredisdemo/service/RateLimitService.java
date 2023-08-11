package com.example.springredisdemo.service;

import com.example.springredisdemo.dto.RequestDescriptor;

import java.util.List;

public interface RateLimitService {
    boolean allowRequest(List<RequestDescriptor> descriptors);
}
