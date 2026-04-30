package com.xzzj.medmanager.common.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordEncoder {

    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;
    private static final int ITERATIONS = 65536;

    public static String encode(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_LENGTH * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hash);
            return saltBase64 + ":" + hashBase64;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("√‹¬Îº”√‹ ß∞‹", e);
        }
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        try {
            String[] parts = encodedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            PBEKeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), salt, ITERATIONS, HASH_LENGTH * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] actualHash = factory.generateSecret(spec).getEncoded();

            return slowEquals(expectedHash, actualHash);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
