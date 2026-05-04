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
                if (line.contains("Product") || line.contains("Product name")) {
                    result.put("name", line.replaceAll(".*[Product]+[::]*", "").trim());
                }
                if (line.contains("Specification")) {
                    result.put("specification", line.replaceAll(".*Specification+[::]*", "").trim());
                }
                if (line.contains("Expiration date")) {
                    result.put("expireDate", line.replaceAll(".*Expiration date+[::]*", "").trim());
                }
                if (line.contains("Manufacturer") || line.contains("Manufacturer name")) {
                    result.put("manufacturer", line.replaceAll(".*(Manufacturer|Manufacturer name)+[::]*", "").trim());
                }
            }
        } catch (Exception e) {
            log.error("OCR解析失败: {}", e.getMessage());
        }

        return result;
    }

    private Map<String, String> mockRecognize() {
        Map<String, String> result = new HashMap<>();
        result.put("name", "");
        result.put("specification", "");
        result.put("expireDate", "");
        result.put("manufacturer", "");
        result.put("rawText", "OCR识别失败");   
        result.put("note", "请检查图片是否清晰，是否包含药品信息");
        return result;
    }
}
