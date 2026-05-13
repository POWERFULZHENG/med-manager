package com.xzzj.medmanager.service;

import com.xzzj.medmanager.dto.LoginRequest;
import com.xzzj.medmanager.vo.LoginVO;

public interface IAuthService {

    LoginVO login(LoginRequest request);

    LoginVO.UserInfoVO getCurrentUser(Long userId);

    void logout(String token);
}
