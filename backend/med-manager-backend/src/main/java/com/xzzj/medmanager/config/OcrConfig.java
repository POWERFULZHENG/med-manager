package com.xzzj.medmanager.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class OcrConfig {

    @Value("${ocr.enabled:true}")
    private Boolean enabled;

    @Value("${ocr.baidu.api-key:}")
    private String baiduApiKey;

    @Value("${ocr.baidu.secret-key:}")
    private String baiduSecretKey;

    @Value("${ocr.baidu.url:https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic}")
    private String baiduUrl;

    @Value("${ocr.baidu.token-url:https://aip.baidubce.com/oauth/2.0/token}")
    private String baiduTokenUrl;
}
