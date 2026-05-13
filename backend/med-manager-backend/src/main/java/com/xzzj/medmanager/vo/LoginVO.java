package com.xzzj.medmanager.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {

    private String token;
    private UserInfoVO user;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfoVO {
        private Long id;
        private String username;
        private String nickname;
        private String roleCode;
    }
}
