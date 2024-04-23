package ru.rmntim.client.lib;

import ru.rmntim.common.commands.Command;
import ru.rmntim.common.network.UserCredentials;

import java.util.List;

@FunctionalInterface
public interface CommandCreator {
    Command create(List<String> args, UserCredentials userCredentials);
}
