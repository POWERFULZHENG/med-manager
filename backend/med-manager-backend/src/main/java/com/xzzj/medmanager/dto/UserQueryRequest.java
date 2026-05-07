package com.xzzj.medmanager.dto;

import lombok.Data;

@Data
public class UserQueryRequest {

    private String username;

    private String nickname;

    private String phone;

    private Integer status;

    private Long roleId;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
