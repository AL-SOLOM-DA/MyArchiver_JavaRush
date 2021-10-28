package com.company.command;

import com.company.ConsoleHelper;
import com.company.ZipFileManager;
import com.company.exception.PathIsNotFoundException;

import java.nio.file.Paths;

public class ZipAddCommand extends ZipCommand{
    @Override
    public void execute() throws Exception {
        ZipFileManager zipFileManager = getZipFileManager();
        ConsoleHelper.writeMessage("Inpuf full path of file: ");
        String pathName = ConsoleHelper.readString();
        try {
            zipFileManager.addFile(Paths.get(pathName));
        } catch (PathIsNotFoundException e){
            e.printStackTrace();
        }
        ConsoleHelper.writeMessage("File added");
    }
}
