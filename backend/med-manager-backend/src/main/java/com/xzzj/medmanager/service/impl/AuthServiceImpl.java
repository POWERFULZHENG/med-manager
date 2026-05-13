package com.xzzj.medmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xzzj.medmanager.common.utils.JwtUtils;
import com.xzzj.medmanager.common.utils.PasswordEncoder;
import com.xzzj.medmanager.common.utils.TokenBlacklist;
import com.xzzj.medmanager.dto.LoginRequest;
import com.xzzj.medmanager.entity.User;
import com.xzzj.medmanager.mapper.UserMapper;
import com.xzzj.medmanager.service.IAuthService;
import com.xzzj.medmanager.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    private static final Map<Long, String> ROLE_CODE_MAP = new HashMap<>();

    static {
        ROLE_CODE_MAP.put(1L, "ADMIN");
        ROLE_CODE_MAP.put(2L, "USER");
    }

    @Override
    public LoginVO login(LoginRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }

        if (!PasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUsername());

        String roleCode = ROLE_CODE_MAP.getOrDefault(user.getRoleId(), "USER");

        LoginVO.UserInfoVO userInfo = new LoginVO.UserInfoVO(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                roleCode
        );

        return new LoginVO(token, userInfo);
    }

    @Override
    public LoginVO.UserInfoVO getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        String roleCode = ROLE_CODE_MAP.getOrDefault(user.getRoleId(), "USER");
        return new LoginVO.UserInfoVO(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                roleCode
        );
    }

    @Override
    public void logout(String token) {
        if (jwtUtils.validateToken(token)) {
            tokenBlacklist.addToken(token);
        }
    }
}
