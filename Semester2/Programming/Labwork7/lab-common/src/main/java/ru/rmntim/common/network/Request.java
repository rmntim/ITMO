package ru.rmntim.common.network;

import ru.rmntim.common.commands.Command;

import java.io.Serializable;

/**
 * Wraps {@link UserCredentials} and {@link Command}.
 *
 * @param userCredentials user credentials
 * @param command         command
 */
public record Request(UserCredentials userCredentials, Command command) implements Serializable {
}
