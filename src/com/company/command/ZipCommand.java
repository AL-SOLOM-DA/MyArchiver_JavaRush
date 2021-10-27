package com.company.command;

import com.company.ConsoleHelper;
import com.company.ZipFileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ZipCommand implements Command{
    public ZipFileManager getZipFileManager() throws Exception {
        ConsoleHelper.writeMessage("Input full path of archive");
         String fullPath = ConsoleHelper.readString();
         return new ZipFileManager(Paths.get(fullPath));

    }
}
