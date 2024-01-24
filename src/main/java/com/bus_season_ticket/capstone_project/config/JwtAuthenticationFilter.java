package com.bus_season_ticket.capstone_project.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component //tell to spring to manage as bean
@RequiredArgsConstructor // create constructors for final private fields.
class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
           @NotNull HttpServletRequest request,
           @NotNull HttpServletResponse response,
           @NotNull FilterChain filterChain
    )throws ServletException, IOException {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final  String userEmail;
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);///to do extract the useremail from JWT token

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {// happening authentication. is this null -> user is not yet authenticated
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); //get user from database
           if(jwtService.isTokenValid(jwt, userDetails)) { // check authenticity of token
               //creates an authentication token (authToken) using userDetails and sets null as the credentials (password). It includes the user's authorities (roles or permissions).
               UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                       userDetails,
                       null,
                       userDetails.getAuthorities() //role
               );

               authToken.setDetails(
                       // It adds additional details to the authentication token by setting WebAuthenticationDetails based on the request. This includes details like the remote address, session ID, and more.
                       new WebAuthenticationDetailsSource().buildDetails(request)
               );
               SecurityContextHolder.getContext().setAuthentication(authToken);
           }
           filterChain.doFilter(request,response);
        }
    }
}
