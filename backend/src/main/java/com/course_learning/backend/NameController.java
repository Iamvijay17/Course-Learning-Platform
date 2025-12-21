package com.course_learning.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class NameController {

    @GetMapping("/name")
    public Map<String, String> getName() {
        return Map.of("message", "hello vijay");
    }
}
