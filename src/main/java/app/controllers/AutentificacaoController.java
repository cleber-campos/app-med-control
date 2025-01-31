package app.controllers;

import app.dtos.autentificacao.AutentificacaoRequestDTO;
import app.models.Usuario;
import app.shared.security.TokenService;
import app.shared.security.TokenJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/login")
public class AutentificacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid AutentificacaoRequestDTO autentificacaoRequest) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    autentificacaoRequest.login(),
                    autentificacaoRequest.senha());
            var autentication = manager.authenticate(authenticationToken);
            var token = tokenService.gerarToken((Usuario) autentication.getPrincipal());
            return ResponseEntity.ok(new TokenJWT(token));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
