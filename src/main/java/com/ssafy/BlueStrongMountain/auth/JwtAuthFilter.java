package com.ssafy.BlueStrongMountain.auth;

import com.ssafy.BlueStrongMountain.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    //filter 미적용 api uri
    private static final List<String> PUBLIC_PATHS = List.of(
            //"/api/v1/auth/refresh",
            "/api/v1/auth/password/reset",
            "/api/v1/auth/existHandle",
            "/api/v1/auth/duplicate/username",
            "/api/v1/auth/register",
            "/api/v1/auth/login"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        try{
            String token = resolveToken(request);

            if(token != null){
                if(!jwtProvider.validateToken(token)){
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                    return;
                }

                Long userId = jwtProvider.getUserIdFromToken(token);
                AuthContext.setUserId(userId);
            }

            //swagger 테스트시 주석처리
            else{
                if(!isPublicPath(request)){
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                    return;
                }
            }

            filterChain.doFilter(request, response);
        }finally {
            AuthContext.clear();
        }
    }
    private String resolveToken(HttpServletRequest req){
        String bearer = req.getHeader("Authorization");
        if(bearer == null || !bearer.startsWith("Bearer")){
            return null;
        }
        return bearer.substring(7);
    }

    private boolean isPublicPath(HttpServletRequest req){
        String uri = req.getRequestURI();
        return  PUBLIC_PATHS.stream().anyMatch(uri::startsWith);
    }
}
