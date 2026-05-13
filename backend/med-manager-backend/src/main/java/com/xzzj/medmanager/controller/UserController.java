package com.xzzj.medmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzzj.medmanager.common.result.R;
import com.xzzj.medmanager.dto.UserQueryRequest;
import com.xzzj.medmanager.dto.UserRequest;
import com.xzzj.medmanager.service.IUserService;
import com.xzzj.medmanager.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public R<IPage<UserVO>> listUsers(UserQueryRequest queryRequest) {
        IPage<UserVO> page = userService.listUsers(queryRequest);
        return R.ok(page);
    }

    @GetMapping("/{id}")
    public R<UserVO> getUserById(@PathVariable Long id) {
        UserVO user = userService.getUserById(id);
        return R.ok(user);
    }

    @PostMapping
    public R<UserVO> createUser(@Valid @RequestBody UserRequest request) {
        UserVO user = userService.createUser(request);
        return R.ok(user);
    }

    @PutMapping("/{id}")
    public R<UserVO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        UserVO user = userService.updateUser(id, request);
        return R.ok(user);
    }

    @DeleteMapping("/{id}")
    public R<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return R.ok();
    }

    @PutMapping("/{id}/status")
    public R<UserVO> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        UserVO user = userService.updateUserStatus(id, status);
        return R.ok(user);
    }
}
