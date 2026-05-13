package com.xzzj.medmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzzj.medmanager.common.utils.PasswordEncoder;
import com.xzzj.medmanager.dto.UserQueryRequest;
import com.xzzj.medmanager.dto.UserRequest;
import com.xzzj.medmanager.entity.User;
import com.xzzj.medmanager.mapper.UserMapper;
import com.xzzj.medmanager.service.IUserService;
import com.xzzj.medmanager.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    private static final Map<Long, String> ROLE_NAME_MAP = new HashMap<>();

    static {
        ROLE_NAME_MAP.put(1L, "管理员");
        ROLE_NAME_MAP.put(2L, "普通用户");
    }

    @Override
    public IPage<UserVO> listUsers(UserQueryRequest queryRequest) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(queryRequest.getUsername())) {
            wrapper.like(User::getUsername, queryRequest.getUsername());
        }
        if (StringUtils.hasText(queryRequest.getNickname())) {
            wrapper.like(User::getNickname, queryRequest.getNickname());
        }
        if (StringUtils.hasText(queryRequest.getPhone())) {
            wrapper.like(User::getPhone, queryRequest.getPhone());
        }
        if (queryRequest.getStatus() != null) {
            wrapper.eq(User::getStatus, queryRequest.getStatus());
        }
        if (queryRequest.getRoleId() != null) {
            wrapper.eq(User::getRoleId, queryRequest.getRoleId());
        }

        wrapper.orderByDesc(User::getCreatedAt);

        Page<User> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        IPage<User> userPage = userMapper.selectPage(page, wrapper);

        return userPage.convert(this::convertToVO);
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    public UserVO createUser(UserRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        if (userMapper.exists(wrapper)) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(request, user);

        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(PasswordEncoder.encode(request.getPassword()));
        } else {
            user.setPassword(PasswordEncoder.encode("123456"));
        }

        userMapper.insert(user);
        return convertToVO(user);
    }

    @Override
    public UserVO updateUser(Long id, UserRequest request) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!user.getUsername().equals(request.getUsername())) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUsername, request.getUsername());
            if (userMapper.exists(wrapper)) {
                throw new RuntimeException("用户名已存在");
            }
            user.setUsername(request.getUsername());
        }

        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        if (request.getRoleId() != null) {
            user.setRoleId(request.getRoleId());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(PasswordEncoder.encode(request.getPassword()));
        }

        userMapper.updateById(user);
        return convertToVO(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getRoleId() == 1L) {
            throw new RuntimeException("不能删除管理员");
        }
        userMapper.deleteById(id);
    }

    @Override
    public UserVO updateUserStatus(Long id, Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getRoleId() == 1L && status == 0) {
            throw new RuntimeException("不能禁用管理员");
        }
        user.setStatus(status);
        userMapper.updateById(user);
        return convertToVO(user);
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        vo.setRoleName(ROLE_NAME_MAP.getOrDefault(user.getRoleId(), "未知"));
        return vo;
    }
}
