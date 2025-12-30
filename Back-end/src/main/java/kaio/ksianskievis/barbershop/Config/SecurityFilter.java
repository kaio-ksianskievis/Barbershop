package kaio.ksianskievis.barbershop.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaio.ksianskievis.barbershop.Repository.UserRepository;
import kaio.ksianskievis.barbershop.Services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private  TokenService tokenService;
    @Autowired
    private  UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Recupera o token da requisição
        var token = this.recoverToken(request);

        // 2. Se o token existir, valida
        if (token != null) {
            var email = tokenService.validateToken(token); // Retorna o email se for válido

            if (!email.isEmpty()) {
                // 3. Busca o usuário no banco (para pegar as roles/permissões)
                UserDetails user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));

                // 4. Salva o usuário no contexto do Spring Security (Autentica ele na requisição atual)
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 5. Segue o fluxo (vai pro próximo filtro ou pro Controller)
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        // O header vem como "Bearer eyJhbGciOiJIUzI1..."
        // Removemos o "Bearer " para pegar só o token
        return authHeader.replace("Bearer ", "");
    }
}