package com.hjy.mtpattern.chap7.pc.example;

import com.hjy.mtpattern.chap7.pc.AbstractTerminatableThread;
import com.hjy.mtpattern.chap7.pc.BlockingQueueChannel;
import com.hjy.mtpattern.chap7.pc.Channel;

import java.io.*;
import java.text.Normalizer;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 17-12-7.
 *  对附件生成全文检索所需的索引文件
 *  模式角色：Producer-Consumer.Producer
 */
public class AttachmentProcessor {

    private final String ATTACHMENT_STORE_BASE_DIR = "/tmp/attachments/";

    //模式角色：Producer-Consumer.Channel
    private final Channel<File> channel = new BlockingQueueChannel<File>(new ArrayBlockingQueue<File>(200));

    //模式角色：Producer-Consumer.Consumer
    private final AbstractTerminatableThread indexingThread = new AbstractTerminatableThread() {
        @Override
        protected void doRun() throws Exception {
            File file = null;

            file = channel.take();
            try {
                indexFile(file);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                terminationToken.reservations.decrementAndGet();
            }
        }

        //根据指定文件生成全文搜索所需的索引文件
        private void indexFile(File file) throws Exception{
            //省略其他代码

            //模拟生成索引文件的时间消耗
            Random rnd = new Random();
            try {
                TimeUnit.MILLISECONDS.sleep(rnd.nextInt(100));
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    };


    public void init(){
        indexingThread.start();
    }

    public void shutdown(){
        indexingThread.terminate();
    }


    public void saveAttachment(InputStream in,String documentId,String originalFileName)throws IOException{
        File file = saveAsFile(in, documentId, originalFileName);
        try {
            channel.put(file);
        }catch (InterruptedException e){
            ;
        }
        indexingThread.terminationToken.reservations.incrementAndGet();
    }


    private File saveAsFile(InputStream in,String documentId,String originalFileName)throws IOException{
        String dirName = ATTACHMENT_STORE_BASE_DIR +documentId;
        File dir = new File(dirName);
        dir.mkdirs();
        File file = new File(dirName+File.separator+ Normalizer.normalize(originalFileName, Normalizer.Form.NFC));

        //防止目录跨越攻击
        if (!dirName.equals(file.getCanonicalFile().getParent())){
            throw new SecurityException("Invalid originalFileName:"+originalFileName);
        }

        BufferedOutputStream bos = null;
        BufferedInputStream bis = new BufferedInputStream(in);
        byte[] buf = new byte[2048];
        int len = -1;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            while ((len = bis.read(buf))>0){
                bos.write(buf,0,len);
            }
            bos.flush();
        }finally {
            try {
                bis.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            try {
                if (null!=bos){
                    bos.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return file;
    }
}
