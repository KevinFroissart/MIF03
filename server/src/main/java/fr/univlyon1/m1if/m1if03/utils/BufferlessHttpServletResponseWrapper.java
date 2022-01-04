package fr.univlyon1.m1if.m1if03.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.PrintWriter;

public class BufferlessHttpServletResponseWrapper extends HttpServletResponseWrapper {
    ServletOutputStream os;

    public BufferlessHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        os = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }

            @Override
            public void write(int b) {
            }
        };
    }

    @Override
    public void setCharacterEncoding(String charset) {
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return os;
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(os);
    }

    @Override
    public void setContentLength(int len) {
    }

    @Override
    public void setContentLengthLong(long len) {
    }

    @Override
    public void setContentType(String type) {
    }

    @Override
    public void setBufferSize(int size) {
    }

    @Override
    public void flushBuffer() {
    }

    @Override
    public void reset() {
    }

    @Override
    public void resetBuffer() {
    }
}