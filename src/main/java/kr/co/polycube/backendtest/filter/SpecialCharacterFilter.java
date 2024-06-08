package kr.co.polycube.backendtest.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SpecialCharacterFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String queryString = request.getQueryString();
        if (queryString != null && queryString.matches(".*[^a-zA-Z0-9&=:/?].*")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid characters in query string");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
