package ru.rmntim.web.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHasher {

    /**
     * Hash a password using BCrypt.
     *
     * @param password The password to hash.
     * @return The hashed password.
     */
    public static String hashPassword(char[] password) {
        // The cost factor determines the amount of computation required for hashing.
        // A higher number means more work, which increases security but also slows down
        // hashing.
        int cost = 12; // This is a commonly used cost factor.

        return BCrypt.withDefaults().hashToString(cost, password);
    }

    /**
     * Verify a password using BCrypt.
     *
     * @param password       The password to verify.
     * @param hashedPassword The hashed password to compare against.
     * @return True if the password matches the hashed password.
     */
    public static boolean checkPassword(char[] password, String hashedPassword) {
        return BCrypt.verifyer().verify(password, hashedPassword).verified;
    }
}
