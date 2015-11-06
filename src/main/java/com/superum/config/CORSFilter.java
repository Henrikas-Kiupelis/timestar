package com.superum.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class CORSFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String origin = request.getHeader("origin");
        if (allowedOrigin(origin)) {
            HttpServletResponse response = (HttpServletResponse) res;
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS, PATCH");
            response.setHeader("Access-Control-Max-Age", "604800");
            response.setHeader("Access-Control-Allow-Headers", "Authorization");
        }
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}

    private final List<String> acceptedOrigins = Arrays.asList(
            "http://localhost",
            "https://localhost",
            "http://timestar.semwin.lt",
            "https://timestar.semwin.lt");

    private boolean allowedOrigin(String origin) {
        if (origin.endsWith("/"))
            origin = origin.substring(0, origin.length() - 1);
        return acceptedOrigins.contains(origin);
    }

}