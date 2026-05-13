package com.xzzj.medmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzzj.medmanager.dto.UserQueryRequest;
import com.xzzj.medmanager.dto.UserRequest;
import com.xzzj.medmanager.entity.User;
import com.xzzj.medmanager.vo.UserVO;

public interface IUserService {

    IPage<UserVO> listUsers(UserQueryRequest queryRequest);

    UserVO getUserById(Long id);

    UserVO createUser(UserRequest request);

    UserVO updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

    UserVO updateUserStatus(Long id, Integer status);
}
