package eu.senlainc.course.foodpricing.controllers;

import eu.senlainc.course.foodpricing.dto.ApiResponse;
import eu.senlainc.course.foodpricing.dto.AuthRequest;
import eu.senlainc.course.foodpricing.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/public")
@Component
public class AuthApi {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        ApiResponse response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }
}