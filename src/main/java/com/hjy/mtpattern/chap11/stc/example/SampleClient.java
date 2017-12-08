package com.hjy.mtpattern.chap11.stc.example;

/**
 * Created by hjy on 17-12-8.
 */
public class SampleClient {
    private static final MessageFileDownloader DOWNLOADER;

    static {
        DOWNLOADER = new MessageFileDownloader("/tmp/incoming","192.168.1.105","username","password");
        DOWNLOADER.init();
    }


    public static void main(String[] args) {
        DOWNLOADER.downloadFile("test.xml");
        //执行其他操作
    }



}
