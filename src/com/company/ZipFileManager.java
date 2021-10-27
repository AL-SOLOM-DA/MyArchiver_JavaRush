package com.company;

import com.company.exception.PathIsNotFoundException;
import com.company.exception.WrongZipFileException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))){
            while (zipInputStream.available()>0){
                ZipEntry entry = zipInputStream.getNextEntry();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                copyData(zipInputStream, bos);

                list.add(new FileProperties(entry.getName(), entry.getSize(), entry.getCompressedSize(), entry.getMethod()));
            }
        }
        return list;
    }
}
