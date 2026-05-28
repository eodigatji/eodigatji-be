package eodigatji.eodigatjiserver.auth.controller;

import eodigatji.eodigatjiserver.auth.dto.EmailSendRequest;
import eodigatji.eodigatjiserver.auth.dto.EmailVerifyRequest;
import eodigatji.eodigatjiserver.auth.dto.SignupRequest;
import eodigatji.eodigatjiserver.auth.service.AuthService;
import eodigatji.eodigatjiserver.auth.service.MailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final MailService mailService;
    private final AuthService authService;

    public AuthController(MailService mailService, AuthService authService) {
        this.mailService = mailService;
        this.authService = authService;
    }

    @PostMapping("/email/send")
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailSendRequest request) {
        mailService.sendVerificationCode(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody EmailVerifyRequest request) {
        mailService.verifyEmail(request.email(), request.code());
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
    }
}
