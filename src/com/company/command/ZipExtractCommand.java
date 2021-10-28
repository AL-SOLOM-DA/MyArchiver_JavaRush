package com.company.command;

import com.company.ConsoleHelper;
import com.company.ZipFileManager;
import com.company.exception.PathIsNotFoundException;

import java.nio.file.Paths;

public class ZipExtractCommand extends ZipCommand{
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Extract archive");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Input full path by folder for archive: ");
            String fileName = ConsoleHelper.readString();
            zipFileManager.extractAll(Paths.get(fileName));
            ConsoleHelper.writeMessage("Archive extracted");
            ConsoleHelper.writeMessage("");
        } catch (PathIsNotFoundException e){
            ConsoleHelper.writeMessage("Wrong file's path!");
        }
    }
}
