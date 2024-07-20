package com.bookshop.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.annotation.WebFilter;

@Component
@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        String loginURI = httpRequest.getContextPath() + "/login";
        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean isLoginPage = httpRequest.getRequestURI().endsWith("loginpage");
        boolean isRegisterPage = httpRequest.getRequestURI().endsWith("register");

        if (isLoginRequest || isLoginPage || isRegisterPage) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null || session.getAttribute("token") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginpage");
            return;
        }

        String token = (String) session.getAttribute("token");
        String rolename = (String) session.getAttribute("rolename");
        String username = (String) session.getAttribute("username");

        try {
            String usernameInToken = extractUsername(token);
            String rolenameInToken = extractRoleFromToken(token);

            if (!rolename.equals(rolenameInToken) || JwtUtil.isTokenExpired(token) || !username.equals(usernameInToken)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginpage");
                return;
            }

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginpage");
        } catch (Exception e) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginpage");
        }
    }

    private String extractRoleFromToken(String token) {
        return JwtUtil.extractClaims(token).get("role",String.class);
    }
    public static String extractUsername(String token) {
        return JwtUtil.extractClaims(token).getSubject();
    }

}
