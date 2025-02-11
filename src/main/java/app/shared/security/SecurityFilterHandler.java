package app.shared.security;

import app.services.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilterHandler extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public SecurityFilterHandler(TokenService tokenService, @Lazy UsuarioService usuarioService) {
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var token = recuperarToken(request); //recupera o token na request

        if(token != null){
            if (tokenService.validaTokenExpirado(token)) throw new
                    RuntimeException("Token expirado!" + token); // Verifica se está expirado

            var login = tokenService.recuperarLogin(token); // recupera o login do token

            var usuario = usuarioService.buscarUsuarioPorLogin(login); // recupera o usuario

            var authentication = new UsernamePasswordAuthenticationToken(
                    usuario, null, usuario.getAuthorities()); // autentica usuario e senha

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);  // autoriza a entrada no filtro do Spring
        }
        filterChain.doFilter(request,response); //efetua a cadeia de filtros
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
