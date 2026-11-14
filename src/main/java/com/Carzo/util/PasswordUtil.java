package com.Carzo.util;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    private static final int ITERATIONS = 310000;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;

    // Generate secure password hash
    public static String hashPassword(String password) {

        try {
            byte[] salt = new byte[SALT_LENGTH];
            new SecureRandom().nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(), salt,
                ITERATIONS, KEY_LENGTH
            );

            SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();
            spec.clearPassword();

            return "pbkdf2$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new IllegalStateException("Password hashing failed", e);
        }
    }

    // Verify password
    public static boolean verifyPassword(
            String password, String storedHash) {

        try {
            String[] parts = storedHash.split("\\$");

            if (parts.length != 4 || !parts[0].equals("pbkdf2")) {
                return false;
            }

            int iterations = Integer.parseInt(parts[1]);

            if (iterations < 100000 || iterations > 1000000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);

            if (salt.length != SALT_LENGTH ||
                expectedHash.length != KEY_LENGTH / 8) {
                return false;
            }

            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(), salt,
                iterations, KEY_LENGTH
            );

            SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] actualHash = factory.generateSecret(spec).getEncoded();
            spec.clearPassword();

            return MessageDigest.isEqual(expectedHash, actualHash);

        } catch (Exception e) {
            return false;
        }
    }
}