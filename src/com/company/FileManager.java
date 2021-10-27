package com.company;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private Path rootPath;
    private List<Path> fileList;

    public FileManager(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        this.fileList = new ArrayList<>();
        collectFileList(rootPath);
    }

    public List<Path> getFileList() {
        return fileList;
    }

    public void collectFileList(Path path) throws IOException{
        if(Files.isRegularFile(path)){
            fileList.add(rootPath.relativize(path));
        }

        else if(Files.isDirectory(path)){
            try(DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path file:stream) {
                    collectFileList(file);
                }
            }
        }
    }
}
