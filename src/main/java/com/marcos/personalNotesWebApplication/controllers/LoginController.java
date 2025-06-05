package com.marcos.personalNotesWebApplication.controllers;

import com.marcos.personalNotesWebApplication.dtos.request.LoginRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.UserResponseDto;
import com.marcos.personalNotesWebApplication.services.LoginService;
import com.marcos.personalNotesWebApplication.services.impl.LoginServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private final LoginService loginService;
    private final LoginServiceImpl loginServiceImpl;

    public LoginController(LoginService loginService, LoginServiceImpl loginServiceImpl) {
        this.loginService = loginService;
        this.loginServiceImpl = loginServiceImpl;
    }
    @PostMapping
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequest) {
        return ResponseEntity.ok(loginService.loginUser(loginRequest));
    }
}
