package com.taurupro.marketplace.infrastructure.security;

import com.taurupro.marketplace.infrastructure.security.jwt.JwtEntryPoint;
import com.taurupro.marketplace.infrastructure.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class MainSecurity {
    @Autowired
    public JwtEntryPoint jwtEntryPoint;


    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.cors( cors-> cors.configurationSource(request -> {
                    CorsConfiguration config  = new CorsConfiguration();
                    config .setAllowedOrigins(List.of("http://localhost:4200"));
                    config .setAllowedMethods(List.of("HEAD","GET","POST","PUT","DELETE"));
                    config .setAllowCredentials(true);
                    config .addExposedHeader("Message");
                    config .setAllowedHeaders(List.of("Authorization","Cache-control", "Content-Type"));
                    return config ;
                })).csrf(csrf->csrf.disable())
                .authorizeHttpRequests(
                        auth->auth.requestMatchers("/catalog/**","/auth/refresh","/breeds","/auth/login","/auth/confirm-password").permitAll().anyRequest().authenticated()
                )
                .exceptionHandling(auth->auth.authenticationEntryPoint(jwtEntryPoint))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
