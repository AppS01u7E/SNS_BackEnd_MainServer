package com.jinwoo.snsbackend_mainserver.global.security.component;

import com.jinwoo.snsbackend_mainserver.global.security.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import neiseApi.exception.InvaildDateException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if ((token != null) && tokenProvider.verifyToken(token)){
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    @ExceptionHandler
    private String getToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) return null;
        if (bearerToken.startsWith("Bearer ")) return bearerToken.substring(7);
        throw new InvaildDateException();
    }
}
