package com.xzzj.medmanager;

import com.xzzj.medmanager.common.utils.PasswordEncoder;
import org.junit.jupiter.api.Test;

public class PasswordGeneratorTest {

    @Test
    public void generatePassword() {
        String password = "123456";
        String encoded = PasswordEncoder.encode(password);
        System.out.println("密码 123456 的哈希值: " + encoded);
        System.out.println("验证结果: " + PasswordEncoder.matches(password, encoded));
    }
}
