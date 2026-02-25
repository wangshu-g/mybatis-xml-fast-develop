package com.wangshu.tool;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public final class FileUtil {
    private FileUtil() {
    }

    public static File touch(String filePath) throws IOException {
        File file = new File(filePath);
        FileUtils.touch(file);
        return file;
    }

    public static File touch(File file) throws IOException {
        FileUtils.touch(file);
        return file;
    }

    public static boolean exist(String filePath) {
        return new File(filePath).exists();
    }

    public static File writeString(String str, File file, Charset charset) throws IOException {
        FileUtils.writeStringToFile(file, str, charset);
        return file;
    }

    public static File writeString(String str, String filePath, Charset charset) throws IOException {
        File file = touch(filePath);
        FileUtils.writeStringToFile(file, str, charset);
        return file;
    }

}
