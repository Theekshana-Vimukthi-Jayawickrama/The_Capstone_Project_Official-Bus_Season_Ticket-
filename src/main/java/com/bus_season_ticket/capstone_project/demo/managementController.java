package com.bus_season_ticket.capstone_project.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/management")
public class managementController {
    @GetMapping
    public String get() {
        return "GET:: admin controller";
    }
    @PostMapping
    public String post() {
        return "Post:: admin controller";
    }
    @PutMapping
    public String put() {
        return "put:: admin controller";
    }
    @DeleteMapping
    public String delete() {
        return "delete:: admin controller";
    }


}
