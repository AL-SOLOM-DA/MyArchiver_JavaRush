package com.company;

public class FileProperties {
    private String name;
    private long size;
    private long compressedSize;
    private int compressionMethod;

    public FileProperties(String name, long size, long compressedSize, int compressionMethod) {
        this.name = name;
        this.size = size;
        this.compressedSize = compressedSize;
        this.compressionMethod = compressionMethod;
    }

    public long getCompressionRation(){
        return 100-((compressedSize *100)/size);
    }


    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public int getCompressionMethod() {
        return compressionMethod;
    }

    @Override
    public String toString() {
        return size>0 ? name + " " + size/1024 + " Kb (" + compressedSize/1024 + " Kb) сжатие: " + getCompressionRation() + "%"
                      : name;
    }
}
