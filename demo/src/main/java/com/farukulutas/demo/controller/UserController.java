package com.farukulutas.demo.controller;

import com.farukulutas.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/run-test")
    public @ResponseBody ResponseEntity<String> runTest(@RequestBody String code) throws IOException, InterruptedException {
        ResponseEntity<String> res = userService.writeToFile(code);
        return res;
    }

}
