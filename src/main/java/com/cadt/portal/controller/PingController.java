package com.cadt.portal.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PingController {

    public Map<String, String> ping() {
        return Map.of("status", "Ok");
    }
}
