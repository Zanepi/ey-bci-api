package com.github.zanepi.eybciapi.utils.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(@SuppressWarnings("NullableProblems") HttpServletRequest request, @SuppressWarnings("NullableProblems") HttpServletResponse response, @SuppressWarnings("NullableProblems") FilterChain filterChain) throws ServletException, IOException {

        try{
            String accessToken = jwtUtil.resolveToken(request);
            if(accessToken ==  null){
                filterChain.doFilter(request,response);
                return;
            }
            System.out.println("token: " + accessToken);
            Claims claims = jwtUtil.resolveClaims(request);
            if(claims != null & jwtUtil.validateClaims(claims)){
                String email = claims.getSubject();
                System.out.println("Alleged email:" + email);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(email,"",new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details",e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            objectMapper.writeValue(response.getWriter(),errorDetails);

        }
        filterChain.doFilter(request,response);
    }
}
