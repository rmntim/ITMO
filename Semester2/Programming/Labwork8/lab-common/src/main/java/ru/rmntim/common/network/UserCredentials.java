package ru.rmntim.common.network;

import java.io.Serializable;

/**
 * Class for representing user credentials.
 *
 * @param username username
 * @param password password
 */
public record UserCredentials(String username, String password) implements Serializable {
}
