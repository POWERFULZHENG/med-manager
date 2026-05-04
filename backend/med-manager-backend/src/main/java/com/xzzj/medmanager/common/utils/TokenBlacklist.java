package com.xzzj.medmanager.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {

    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    // @Autowired
    // private JwtUtils jwtUtils;

    public void addToken(String token) {
        blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }

    // public void removeExpiredTokens() {
    //     blacklist.removeIf(token -> !JwtUtils.validateToken(token));
    // }
}
