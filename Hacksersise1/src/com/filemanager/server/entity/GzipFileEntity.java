package com.filemanager.server.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * This is the Gzip file entity class
 * @author Anubhav
 *
 */
public class GzipFileEntity extends GzipEntity {

    protected final File file;
    protected final boolean isGzipFile;

    
    public GzipFileEntity(final File file, String contentType, boolean isGzipFile) {
        super();
        if (file == null) {
            throw new IllegalArgumentException("File may not be null");
        }
        this.file = file;
        this.isGzipFile = isGzipFile;
        setContentType(contentType);
    }

    @Override
    public long getContentLength() {
        return isGzipFile ? this.file.length() : -1;
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream instream = new FileInputStream(this.file);
        if (!isGzipFile) {
            outstream = new GZIPOutputStream(outstream);
        }
        try {
            copy(instream, outstream);
        } finally {
            instream.close();
        }
    }

}
