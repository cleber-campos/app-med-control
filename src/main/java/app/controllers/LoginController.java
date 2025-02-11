package app.controllers;

import app.dtos.login.loginRequestDTO;
import app.services.LoginService;
import app.shared.security.TokenDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<TokenDTO> efetuarLogin(@RequestBody @Valid loginRequestDTO request) {
        return ResponseEntity.ok(loginService.efetuarLogin(request));
    }

}
