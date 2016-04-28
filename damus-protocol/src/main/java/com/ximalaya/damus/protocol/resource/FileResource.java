package com.ximalaya.damus.protocol.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.ximalaya.damus.protocol.Serializable;

public class FileResource<T extends Serializable> extends Resource<T> {

    public FileResource(String path, Class<T> clz) {
        super(path, clz);
    }

    @Override
    public String getPath() {
        return path;
    }

    private InputStream is = null;
    private OutputStream os = null;

    @Override
    public InputStream getInputStream() throws IOException {
        if (is == null) {
            String fileName = getPathWithoutHeader();
            is = new BufferedInputStream(new FileInputStream(new File(fileName)));
        }
        return is;
    }

    @Override
    public void closeInputStream() {
        IOUtils.closeQuietly(is);
        is = null;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (os == null) {
            String fileName = getPathWithoutHeader();
            os = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
        }
        return os;
    }

    @Override
    public void remove() throws IOException {
        String fileName = getPathWithoutHeader();
        FileUtils.deleteDirectory(new File(fileName));
    }

    @Override
    public boolean exists() throws IOException {
        String fileName = getPathWithoutHeader();
        return new File(fileName).exists();
    }

    @Override
    public void closeOutputStream() {
        IOUtils.closeQuietly(os);
        os = null;
    }

    @Override
    public void close() throws IOException {
        closeInputStream();
        closeOutputStream();
    }

    private String getPathWithoutHeader() {
        String path = getPath();
        return path != null ? path.replaceAll("file:", "") : path;
    }
}
