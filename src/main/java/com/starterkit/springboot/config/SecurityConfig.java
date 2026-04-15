package com.starterkit.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Credenciais fixas (para testes/ensino)
        auth.inMemoryAuthentication()
            .withUser("admin").password("admin123").roles("ADMIN")
            .and()
            .withUser("user").password("user123").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()

            .authorizeRequests()

                // --- ENDPOINTS PÚBLICOS (sem login) ---
                // exemplo 1: permitir listar e ver um todo sem autenticação
                .antMatchers(HttpMethod.GET, "/api/todos/**").permitAll()

                // exemplo 2: permitir health/actuator sem autenticação (se usares)
                .antMatchers("/actuator/health").permitAll()

                // --- RESTO COM LOGIN ---
                // criar/alterar/apagar só com login (ex.: ADMIN)
                //.antMatchers(HttpMethod.POST, "/api/todos/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/todos/**").permitAll()

                .antMatchers(HttpMethod.PUT, "/api/todos/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/todos/**").hasRole("ADMIN")

                // qualquer outra coisa pede autenticação
                .anyRequest().authenticated()

            .and()
            .httpBasic();
    }

    // Para "ensino": passwords em texto simples. Em produção usa BCrypt.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
