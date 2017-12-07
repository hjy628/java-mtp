package com.hjy.mtpattern.chap8.activeobject.example;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by hjy on 17-12-7.
 */
public class Attachment implements Serializable{
    private static final long serialVersionUID = -2374475206285236854L;

    private String contentType;
    private byte[] content;


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "contentType='" + contentType + '\'' +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
