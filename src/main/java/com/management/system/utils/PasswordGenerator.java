package com.management.system.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";

    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        // Add 6 random letters
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(LETTERS.length());
            password.append(LETTERS.charAt(index));
        }

        // Add 2 random numbers
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(NUMBERS.length());
            password.append(NUMBERS.charAt(index));
        }

        // Add 2 random special characters
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(SPECIAL_CHARACTERS.length());
            password.append(SPECIAL_CHARACTERS.charAt(index));
        }

        // Convert to a list and shuffle to mix the characters
        List<Character> passwordChars = new ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            passwordChars.add(c);
        }
        Collections.shuffle(passwordChars);

        // Convert back to a string
        StringBuilder shuffledPassword = new StringBuilder();
        for (char c : passwordChars) {
            shuffledPassword.append(c);
        }

        return shuffledPassword.toString();
    }

}