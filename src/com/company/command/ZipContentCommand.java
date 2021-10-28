package com.company.command;

import com.company.ConsoleHelper;
import com.company.FileProperties;
import com.company.ZipFileManager;

import java.util.List;

public class ZipContentCommand extends ZipCommand{
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Showing archive properties: ");
        ZipFileManager zipFileManager = getZipFileManager();
        ConsoleHelper.writeMessage("Properties: ");
        List<FileProperties> properties = zipFileManager.getFileList();
        for (FileProperties filePropertie: properties) {
            ConsoleHelper.writeMessage(filePropertie.toString());
        }
        ConsoleHelper.writeMessage("Archive properties read.\n");
    }
}
