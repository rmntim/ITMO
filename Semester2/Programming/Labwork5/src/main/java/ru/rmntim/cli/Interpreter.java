package ru.rmntim.cli;

import ru.rmntim.cli.commands.Command;
import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.exceptions.BuildCancelledException;
import ru.rmntim.cli.exceptions.ExitException;
import ru.rmntim.cli.exceptions.InvalidScriptException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

public class Interpreter {
    private static final String PS1 = "$ ";
    private final Map<String, Command> commands;

    public Interpreter(final Map<String, Command> commands) {
        this.commands = commands;
    }

    public void run() {
        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            for (; ; ) {
                if (!(System.in instanceof FileInputStream)) {
                    System.out.print(PS1);
                }
                input = reader.readLine();
                if (input == null) {
                    break;
                }
                var userCommand = Arrays.stream(input.trim().split(" ", 2)).map(String::trim).toList();
                var commandName = userCommand.get(0);
                if (!commands.containsKey(commandName)) {
                    eprintln("Command doesn't exist: " + commandName);
                    continue;
                }
                try {
                    commands.get(commandName).execute(userCommand.subList(1, userCommand.size()), reader);
                } catch (BadCommandArgumentsException bcae) {
                    eprintln(bcae.getMessage());
                } catch (BuildCancelledException bce) {
                    System.out.println("Object build cancelled: " + bce.getMessage());
                } catch (InvalidScriptException ise) {
                    eprintln("Script format error: " + ise.getMessage());
                }
            }
        } catch (IOException ioe) {
            eprintln("Error getting input: " + ioe.getMessage());
        } catch (ExitException e) {
            // ignore
        }
    }

    private void eprintln(String message) {
        System.out.println("[ERROR]: " + message);
    }
}
