package kaio.ksianskievis.barbershop.Config;

import kaio.ksianskievis.barbershop.Config.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired // <--- ADICIONE ISSO
    private SecurityFilter securityFilter; // <--- E ISSO

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Desabilita proteção de formulário (pois é API REST)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Não guarda sessão (usa Token)
                .authorizeHttpRequests(authorize -> authorize
                        // Qualquer um pode fazer login ou cadastro
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // Só o BARBEIRO (ADMIN) pode ver lista completa ou deletar
                        .requestMatchers(HttpMethod.GET, "/agendamentos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/agendamentos/**").hasRole("ADMIN")

                        // O USUÁRIO comum pode ver horários livres e criar agendamento
                        .requestMatchers(HttpMethod.GET, "/agendamentos/disponibilidade").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/agendamentos").hasRole("USER")

                        .anyRequest().authenticated() // O resto precisa estar logado
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Seu filtro de token
                .build();
    }

    @Bean // Necessário para criptografar senhas
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}