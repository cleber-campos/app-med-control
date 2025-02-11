package app.services;

import app.dtos.login.loginRequestDTO;
import app.models.Usuario;
import app.repositories.UsuarioRepository;
import app.shared.security.TokenDTO;
import app.shared.security.TokenService;
import app.shared.utils.TimeZoneConfig;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class LoginService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginService(UsuarioRepository usuarioRepository,
                        @Lazy AuthenticationManager authenticationManager, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return usuarioRepository.findByLogin(login);
    }

    public TokenDTO efetuarLogin(loginRequestDTO request) {
            var authorization = new UsernamePasswordAuthenticationToken(request.login(), request.senha());
            var autentication = authenticationManager.authenticate(authorization);
            var token = tokenService.gerarToken((Usuario) autentication.getPrincipal());
            return new TokenDTO(token, converteParaDataLocal(tokenService.
                    recuperarDataExpiracao(token)));
        }

    private LocalDateTime converteParaDataLocal(Instant instant){
        return LocalDateTime.ofInstant(instant, TimeZoneConfig.DEFAULT_ZONE_OFFSET);
    }

}
