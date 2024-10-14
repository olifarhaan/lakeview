package com.olifarhaan.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This filter is called when an exception is throws by other filters in the
 * filter chain. It has been added
 * specifially to catch authentication exceptions thrown by the
 * JwtAuthenticationTokenFilter and to return a
 * 401 Unauthorized status code and a JSON body with the error message and the
 * status code.
 */
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request,
            @SuppressWarnings("null") HttpServletResponse response, @SuppressWarnings("null") FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException ae) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(),
                    new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", ae.getMessage()));
        } catch (Throwable e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(),
                    new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                            e.getMessage()));
        }
    }
}
