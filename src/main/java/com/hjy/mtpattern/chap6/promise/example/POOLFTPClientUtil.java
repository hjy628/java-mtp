package com.hjy.mtpattern.chap6.promise.example;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by hjy on 17-12-6.
 * FTP客户端工具类源码
 * 用线程池执行异步任务
 *　模式角色:Promise.Promisor、Promise.Result
 */
public class POOLFTPClientUtil {

    private volatile static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() * 2, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        },new ThreadPoolExecutor.CallerRunsPolicy());
    }




    private final FTPClient ftp = new FTPClient();

    private final Map<String,Boolean> dirCreateMap = new HashMap<String,Boolean>();

    private POOLFTPClientUtil() {

    }

    //模式角色:Promise.Promisor.compute
    public static Future<POOLFTPClientUtil> newInstance(final String ftpServer, final String userName, final String password){
        Callable<POOLFTPClientUtil> callable = new Callable<POOLFTPClientUtil>() {
            @Override
            public POOLFTPClientUtil call() throws Exception {
                POOLFTPClientUtil self = new POOLFTPClientUtil();
                self.init(ftpServer, userName, password);
                return self;
            }
        };
        //task相当于模式角色:Promise.Promise
        final FutureTask<POOLFTPClientUtil> task = new FutureTask<POOLFTPClientUtil>(callable);

        threadPoolExecutor.execute(task);
        return task;
    }

    private void init(String ftpServer, String userName, String password) throws Exception{
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);

        int reply;
        ftp.connect(ftpServer);

        System.out.println(ftp.getReplyString());

        reply = ftp.getReplyCode();

        if (!FTPReply.isPositiveCompletion(reply)){
            ftp.disconnect();
            throw new RuntimeException("FTP server refused connection.");
        }

        boolean isOK = ftp.login( userName, password);
        if (isOK){
            System.out.println(ftp.getReplyString());
        }else {
            throw new RuntimeException("Failed to login."+ftp.getReplyString());
        }

        reply = ftp.cwd("~/subspsync");
        if (!FTPReply.isPositiveCompletion(reply)){
            ftp.disconnect();
            throw new RuntimeException("Failed to change working directory.reply:"+reply);
        }else {
            System.out.println(ftp.getReplyString());
        }
        ftp.setFileType(FTP.ASCII_FILE_TYPE);
    }


    public void upload(File file)throws Exception{
        InputStream dataIn = new BufferedInputStream(new FileInputStream(file),1024*8);
        boolean isOK;
        String dirName = file.getParentFile().getName();
        String fileName = dirName + File.separator + file.getName();
        ByteArrayInputStream checkFileInputStream = new ByteArrayInputStream("".getBytes());
        try {
            if (!dirCreateMap.containsKey(dirName)){
                ftp.makeDirectory(dirName);
                dirCreateMap.put(dirName,null);
            }

            try {
                isOK = ftp.storeFile(fileName,dataIn);
            }catch (IOException e){
                throw new RuntimeException("Failed to upload "+fileName,e);
            }
            if (isOK){
                ftp.storeFile(fileName+".data",checkFileInputStream);
            }else {
                throw new RuntimeException("Failed to upload "+file + ",reply:"+","+ftp.getReplyString());
            }
        }finally {
            dataIn.close();
        }
    }


    public void disconnect(){
        if (ftp.isConnected()){
            try {
                ftp.disconnect();
            }catch (IOException e){
                //do nothing
            }
        }
    }



}
