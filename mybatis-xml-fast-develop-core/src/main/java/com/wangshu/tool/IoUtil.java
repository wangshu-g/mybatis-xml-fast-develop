package com.wangshu.tool;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

public final class IoUtil {

    private IoUtil() {
    }

    public static String read(InputStream inputStream, Charset charset) throws IOException {
        if (Objects.isNull(inputStream)) {
            return "";
        }
        return IOUtils.toString(inputStream, charset);
    }

}
