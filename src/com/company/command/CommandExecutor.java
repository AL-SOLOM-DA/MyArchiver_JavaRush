package com.company.command;

import com.company.Operation;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {

    private static Map<Operation, Command> ALL_KNOW_COMMANDS_MAP = new HashMap<>();

    static {
        ALL_KNOW_COMMANDS_MAP.put(Operation.ADD, new ZipAddCommand());
        ALL_KNOW_COMMANDS_MAP.put(Operation.CREATE, new ZipCreateCommand());
        ALL_KNOW_COMMANDS_MAP.put(Operation.REMOVE, new ZipRemoveCommand());
        ALL_KNOW_COMMANDS_MAP.put(Operation.EXTRACT, new ZipExtractCommand());
        ALL_KNOW_COMMANDS_MAP.put(Operation.CONTENT, new ZipContentCommand());
        ALL_KNOW_COMMANDS_MAP.put(Operation.EXIT, new ExitCommand());
    }

    public static void execute(Operation operation) throws Exception {
        ALL_KNOW_COMMANDS_MAP.get(operation).execute();
    }

    private CommandExecutor() {
    }
}
