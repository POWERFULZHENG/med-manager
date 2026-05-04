package com.xzzj.medmanager.controller;

import com.xzzj.medmanager.common.result.R;
import com.xzzj.medmanager.common.utils.JwtUtils;
import com.xzzj.medmanager.dto.LoginRequest;
import com.xzzj.medmanager.service.IAuthService;
import com.xzzj.medmanager.vo.LoginVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public R<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        LoginVO loginVO = authService.login(request);
        return R.ok(loginVO);
    }

    @GetMapping("/me")
    public R<LoginVO.UserInfoVO> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return R.fail(40003, "token 无效");
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return R.fail(40003, "token 无效");
        }



        Long userId = jwtUtils.getUserIdFromToken(token);
        LoginVO.UserInfoVO userInfo = authService.getCurrentUser(userId);
        if (userInfo == null) {
            return R.fail(40003, "用户不存在");
        }

        return R.ok(userInfo);
    }

    @PostMapping("/logout")
    public R<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token);
        }
        return R.ok();
    }
}
