package com.github.iamhi.hizone.station.core.utils;

public interface FileService {

    String writeFile(String filename, String application, boolean userBound, byte[] content);

    byte[] readFileContent(String filename, String application, boolean userBound);
}
