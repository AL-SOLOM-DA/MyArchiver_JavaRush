package com.company.command;

import com.company.ConsoleHelper;
import com.company.ZipFileManager;

import java.nio.file.Paths;

public class ZipRemoveCommand extends ZipCommand{
    @Override
    public void execute() throws Exception {
        ZipFileManager zipFileManager = getZipFileManager();
        ConsoleHelper.writeMessage("Inpuf path of file: ");
        String pathName = ConsoleHelper.readString();
        zipFileManager.removeFile(Paths.get(pathName));
        ConsoleHelper.writeMessage("File remove");
    }
}
