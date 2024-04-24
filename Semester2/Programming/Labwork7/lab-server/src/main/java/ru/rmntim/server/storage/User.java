package ru.rmntim.server.storage;

/**
 * Represents user in database.
 *
 * @param username     username
 * @param passwordHash password hash
 */
public record User(String username, byte[] passwordHash) {
}
