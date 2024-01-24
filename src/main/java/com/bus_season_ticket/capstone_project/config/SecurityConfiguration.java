package com.bus_season_ticket.capstone_project.config;

import com.bus_season_ticket.capstone_project.User.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.bus_season_ticket.capstone_project.User.Permission.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableGlobalAuthentication
public class SecurityConfiguration {

    private final  JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**")
                .permitAll()
                .requestMatchers("/api/v1/admin/**")
                .permitAll()
                .requestMatchers("/api/v1/payment/**")
                .permitAll()
                .requestMatchers("/api/v1/route/**")
                .permitAll()
                .requestMatchers("api/v1/journey/**")
                .permitAll()
//                .requestMatchers("/api/v1/user/**")
//                .permitAll()
                .requestMatchers("api/v1/subscription/**")
                .permitAll()
                .requestMatchers("/api/v1/management/**").hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())

                .requestMatchers(GET,"/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(),Manager_READ.name())
                .requestMatchers(POST,"/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(),Manager_CREATE.name())
                .requestMatchers(PUT,"/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(),Manager_UPDATE.name())
                .requestMatchers(DELETE,"/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(),Manager_DELETE.name())

//                .requestMatchers("/api/v1/admin/**").hasAnyRole(ADMIN.name(),MANAGER.name())
//
//                .requestMatchers(GET,"/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
//                .requestMatchers(POST,"/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
//                .requestMatchers(PUT,"/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
//                .requestMatchers(DELETE,"/api/v1/admin/**").hasAuthority(ADMIN_READ.name())

                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
