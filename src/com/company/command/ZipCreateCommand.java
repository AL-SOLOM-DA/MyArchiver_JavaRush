package com.company.command;

import com.company.ConsoleHelper;
import com.company.ZipFileManager;
import com.company.exception.PathIsNotFoundException;

import java.nio.file.Paths;

public class ZipCreateCommand extends ZipCommand{
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Create archive");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Input full path file for archive: ");
            String fileName = ConsoleHelper.readString();
            zipFileManager.createZip(Paths.get(fileName));
            ConsoleHelper.writeMessage("Archive created");
            ConsoleHelper.writeMessage("");
        } catch (PathIsNotFoundException e){
            ConsoleHelper.writeMessage("Wrong file's path!");
        }
    }


}
