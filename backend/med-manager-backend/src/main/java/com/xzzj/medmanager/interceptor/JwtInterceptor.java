package com.xzzj.medmanager.interceptor;

import com.xzzj.medmanager.common.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtUtils.validateToken(token)) {
                Long userId = jwtUtils.getUserIdFromToken(token);
                String username = jwtUtils.getUsernameFromToken(token);
                request.setAttribute("userId", userId);
                request.setAttribute("username", username);
                return true;
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"Î´ÊÚÈ¨£¬ÇëÏÈµÇÂ¼\"}");
        return false;
    }
}
