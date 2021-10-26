package com.company;

import com.company.command.CommandExecutor;
import com.company.command.ExitCommand;
import com.company.exception.WrongZipFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Archiver {

    public static void main(String[] args) throws Exception {
       /* try
            (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            System.out.println("Input archive's path: ");
            ZipFileManager zipFileManager = new ZipFileManager(Paths.get(reader.readLine()));

            System.out.println("Input file's path: ");
            zipFileManager.createZip(Paths.get(reader.readLine()));

        }*/
        Operation operation = null;
        do{
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);
            } catch (WrongZipFileException e){
                ConsoleHelper.writeMessage("You didn't choose archive's file or choose wrong fail");
            } catch (Exception e){
                ConsoleHelper.writeMessage("Critical error! Check input data");
            }
        } while (operation != Operation.EXIT);
    }

    public static Operation askOperation() throws IOException{
        ConsoleHelper.writeMessage("Choose operation: ");
        ConsoleHelper.writeMessage("Create archive - " + Operation.CREATE.ordinal());
        ConsoleHelper.writeMessage("Add file to archive - " + Operation.ADD.ordinal());
        ConsoleHelper.writeMessage("Remove file from archive - " + Operation.REMOVE.ordinal());
        ConsoleHelper.writeMessage("Extract archive - " + Operation.EXTRACT.ordinal());
        ConsoleHelper.writeMessage("Show content of archive - " + Operation.CONTENT.ordinal());
        ConsoleHelper.writeMessage("Exit archive - " + Operation.EXIT.ordinal());

        return Operation.values()[ConsoleHelper.readInt()];
    }
}
