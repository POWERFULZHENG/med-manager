package com.xzzj.medmanager;

import com.xzzj.medmanager.common.utils.JwtUtils;
import com.xzzj.medmanager.common.utils.PasswordEncoder;
import com.xzzj.medmanager.dto.LoginRequest;
import com.xzzj.medmanager.entity.User;
import com.xzzj.medmanager.mapper.UserMapper;
import com.xzzj.medmanager.service.IAuthService;
import com.xzzj.medmanager.vo.LoginVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthLoginTest {

    @Autowired
    private IAuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @MockBean
    private UserMapper userMapper;

    @Test
    public void testPasswordEncoder() {
        String password = "123456";
        String encoded = PasswordEncoder.encode(password);
        System.out.println("Encoded password: " + encoded);
        assertTrue(PasswordEncoder.matches(password, encoded));
        assertFalse(PasswordEncoder.matches("wrong", encoded));
    }

    @Test
    public void testJwtToken() {
        String token = jwtUtils.generateToken(1L, "admin");
        System.out.println("JWT Token: " + token);
        assertTrue(jwtUtils.validateToken(token));
        assertEquals("admin", jwtUtils.getUsernameFromToken(token));
        assertEquals(1L, jwtUtils.getUserIdFromToken(token));
    }

    @Test
    public void testLoginFlow() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword(PasswordEncoder.encode("123456"));
        user.setNickname("系统管理员");
        user.setRoleId(1L);
        user.setStatus(1);

        when(userMapper.selectOne(any())).thenReturn(user);

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("123456");

        LoginVO result = authService.login(request);
        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals("admin", result.getUser().getUsername());
        assertEquals("ADMIN", result.getUser().getRoleCode());
        System.out.println("Login successful! Token: " + result.getToken());
    }
}
