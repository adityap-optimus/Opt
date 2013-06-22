package com.filemanager.server.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.http.Header;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicHeader;
import com.filemanager.utils.Constants.Config;

/**
 * THis is the GZIP compression entity class
 * @author Anubhav
 *
 */
public abstract class GzipEntity extends AbstractHttpEntity implements Cloneable {

    /**
     * Copies one stream to another
     * @warning When outstream is GZIPOutputStream, it will call finish(). But won't close any stream.
     * @param instream 
     * @param outstream
     * @throws IOException 
     */
    protected void copy(InputStream instream, OutputStream outstream) throws IOException {
        byte[] tmp = new byte[Config.BUFFER_LENGTH];
        int l;
        while ((l = instream.read(tmp)) != -1) {
            outstream.write(tmp, 0, l);
        }
        if (outstream instanceof GZIPOutputStream) {
            ((GZIPOutputStream) outstream).finish();
        }
        outstream.flush();
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public InputStream getContent() throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        writeTo(buf);
        return new ByteArrayInputStream(buf.toByteArray());
    }

    @Override
    public Header getContentEncoding() {
        return new BasicHeader("Content-Encoding", "gzip");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
