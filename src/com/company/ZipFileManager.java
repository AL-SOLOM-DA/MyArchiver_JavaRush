package com.company;

import com.company.exception.PathIsNotFoundException;
import com.company.exception.WrongZipFileException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception{
        try (InputStream inputStream = Files.newInputStream(filePath.resolve(fileName))){
            ZipEntry zipEntry = new ZipEntry(fileName.toString());
            zipOutputStream.putNextEntry(zipEntry);

            copyData(inputStream, zipOutputStream);

            zipOutputStream.closeEntry();
        }
    }

    private void copyData(InputStream in, OutputStream out) throws Exception{
        byte[] buf = new byte[8*1024];
        int len;
        while ((len = in.read(buf))>0){
            out.write(buf, 0, len);
        }
    }

    public void createZip(Path source) throws Exception{

        if(Files.notExists(zipFile.getParent())) Files.createDirectory(zipFile.getParent());

        try(ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            if(Files.isRegularFile(source)) {
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            }
            if(Files.isDirectory(source)){
                List<Path> fileNames = new FileManager(source).getFileList();
                for (Path fileName : fileNames) {
                    addNewZipEntry(zipOutputStream, source, fileName);
                }
            }
            else {
                throw new PathIsNotFoundException();
            }
        }
    }

    public List<FileProperties> getFileList() throws Exception{
        if(!Files.isRegularFile(zipFile)){
            throw new WrongZipFileException();
        }
        List<FileProperties> list = new ArrayList<>();
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry entry = zipInputStream.getNextEntry();

            while (entry != null) {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                copyData(zipInputStream, bos);

                list.add(new FileProperties(entry.getName(), entry.getSize(), entry.getCompressedSize(), entry.getMethod()));
                entry = zipInputStream.getNextEntry();
            }
        }

        return list;
    }

    public void extractAll(Path outputFolder) throws Exception{
        if(!Files.isRegularFile(zipFile)) throw new WrongZipFileException();

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))){

            if(Files.notExists(outputFolder)) Files.createDirectory(outputFolder);
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry!=null){
                String fileName = entry.getName();
                Path fileFullName = outputFolder.resolve(fileName);

                Path parent = fileFullName.getParent();
                if (Files.notExists(parent)) Files.createDirectory(parent);
                try(OutputStream outputStream = Files.newOutputStream(fileFullName)) {
                    copyData(zipInputStream, outputStream);
                 }
                entry = zipInputStream.getNextEntry();
            }
        }
    }

    public void removeFiles(List<Path> pathList) throws Exception{
        if(!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }
        /*Path tempFilePath = Files.createTempFile("file",".tmp");

        try(ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempFilePath))){
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

                ZipEntry entry = zipInputStream.getNextEntry();
                while (entry != null) {
                    Path archiveFile = Paths.get(entry.getName());
                    if (!pathList.contains(archiveFile)) {
                        String fileName = entry.getName();
                        zipOutputStream.putNextEntry(new ZipEntry(fileName));
                        copyData(zipInputStream, zipOutputStream);
                        zipOutputStream.closeEntry();
                        zipInputStream.closeEntry();
                    } else {

                        ConsoleHelper.writeMessage("We are removed " + entry.getName());

                    }

                    entry = zipInputStream.getNextEntry();
                }
            }
        }
        Files.move(tempFilePath, zipFile, StandardCopyOption.REPLACE_EXISTING);*/
        Path tempFilePath = Files.createTempFile("file",".tmp");
        try(ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))){
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry!=null){
                if(pathList.contains(Paths.get(entry.getName()))){
                    ConsoleHelper.writeMessage("We are removed " + entry.getName());
                } else {
                    try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempFilePath))){
                        zipOutputStream.putNextEntry(new ZipEntry(entry.getName()));
                        copyData(zipInputStream, zipOutputStream);
                        zipOutputStream.closeEntry();
                        zipInputStream.closeEntry();
                    }
                }
                entry = zipInputStream.getNextEntry();
            }


        }
        Files.move(tempFilePath, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void removeFile(Path path) throws Exception{
        removeFiles(Collections.singletonList(path));
    }

    public void addFiles(List<Path> absolutePathList) throws Exception{
        if(!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }

        Path tempFile = Files.createTempFile("file", ".temp");
        List<String> tempList = new ArrayList<>();
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile));
             ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempFile))){
            ZipEntry entry = zipInputStream.getNextEntry();

            while (entry!=null){
                zipOutputStream.putNextEntry(new ZipEntry(entry.getName()));
                copyData(zipInputStream, zipOutputStream);
                tempList.add(entry.getName());
                zipOutputStream.closeEntry();
                zipInputStream.closeEntry();

                entry = zipInputStream.getNextEntry();
            }
            for (Path file:absolutePathList) {
                if (!Files.isRegularFile(file)||Files.notExists(file)) throw new PathIsNotFoundException();

                if (tempList.contains(file.getFileName())){
                    ConsoleHelper.writeMessage("That file is already in archive");
                } else
                    addNewZipEntry(zipOutputStream, file.getParent(), file.getFileName());

            }
        }
        Files.move(tempFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void addFile(Path path) throws Exception{
        addFiles(Collections.singletonList(path));
    }

}
