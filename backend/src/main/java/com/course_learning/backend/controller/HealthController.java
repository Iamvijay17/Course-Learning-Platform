package com.course_learning.backend.controller;

import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API is up and running...");
        response.put("time", Instant.now().toString());
        return response;
    }
}
