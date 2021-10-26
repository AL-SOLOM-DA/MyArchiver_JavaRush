package com.company;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public void createZip(Path source) throws Exception{
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile));
            InputStream inputStream = Files.newInputStream(source)) {

            ZipEntry zipEntry = new ZipEntry(source.getFileName().toString());
            zipOutputStream.putNextEntry(zipEntry);

            byte[] buffer = new byte[8*1024];
            int len;
            while ((len = inputStream.read(buffer))>0) {

                zipOutputStream.write(buffer,0, len);
            }
            zipOutputStream.closeEntry();
        }

    }
}
