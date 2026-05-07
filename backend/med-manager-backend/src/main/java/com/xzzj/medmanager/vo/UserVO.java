package com.xzzj.medmanager.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    private String phone;

    private Long roleId;

    private String roleName;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
