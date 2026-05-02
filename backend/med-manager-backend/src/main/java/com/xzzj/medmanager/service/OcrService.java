package com.xzzj.medmanager.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzzj.medmanager.config.OcrConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrService {

    private final OcrConfig ocrConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private String cachedToken;
    private long tokenExpireTime;

    public Map<String, String> recognizeMedicine(String imageBase64) {
        if (!ocrConfig.getEnabled()) {
            return mockRecognize();
        }

        try {
            String token = getAccessToken();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("image", imageBase64);
            body.add("language_type", "CHN_ENG");
            body.add("detect_direction", "true");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            String url = ocrConfig.getBaiduUrl() + "?access_token=" + token;

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            return parseOcrResult(response.getBody());
        } catch (Exception e) {
            log.error("OCR识别失败: {}", e.getMessage());
            return mockRecognize();
        }
    }

    private String getAccessToken() {
        if (cachedToken != null && System.currentTimeMillis() < tokenExpireTime) {
            return cachedToken;
        }

        try {
            String url = String.format("%s?grant_type=client_credentials&client_id=%s&client_secret=%s",
                    ocrConfig.getBaiduTokenUrl(),
                    ocrConfig.getBaiduApiKey(),
                    ocrConfig.getBaiduSecretKey());

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            
            cachedToken = root.get("access_token").asText();
            tokenExpireTime = System.currentTimeMillis() + root.get("expires_in").asLong() * 1000 - 60000;
            
            return cachedToken;
        } catch (Exception e) {
            log.error("获取OCR Token失败: {}", e.getMessage());
            return "";
        }
    }

    private Map<String, String> parseOcrResult(String responseBody) {
        Map<String, String> result = new HashMap<>();
        List<String> lines = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode wordsResult = root.get("words_result");
            
            if (wordsResult != null && wordsResult.isArray()) {
                for (JsonNode item : wordsResult) {
                    lines.add(item.get("words").asText());
                }
            }

            result.put("rawText", String.join("\n", lines));
            
            for (String line : lines) {
                if (line.contains("名称") || line.contains("品名")) {
                    result.put("name", line.replaceAll(".*[名称品名]+[:：]*", "").trim());
                }
                if (line.contains("规格")) {
                    result.put("specification", line.replaceAll(".*规格+[:：]*", "").trim());
                }
                if (line.contains("有效期") || line.contains("有效期至")) {
                    result.put("expireDate", line.replaceAll(".*有效期[至]*[:：]*", "").trim());
                }
                if (line.contains("生产") || line.contains("厂家")) {
                    result.put("manufacturer", line.replaceAll(".*(生产|厂家)+[:：]*", "").trim());
                }
            }
        } catch (Exception e) {
            log.error("解析OCR结果失败: {}", e.getMessage());
        }

        return result;
    }

    private Map<String, String> mockRecognize() {
        Map<String, String> result = new HashMap<>();
        result.put("name", "");
        result.put("specification", "");
        result.put("expireDate", "");
        result.put("manufacturer", "");
        result.put("rawText", "OCR服务未配置，请手动输入");
        result.put("note", "演示模式 - 请配置百度OCR服务启用自动识别");
        return result;
    }
}
