package com.hjy.mtpattern.chap14.example;

import com.hjy.mtpattern.chap14.hsha.AsyncTask;
import com.hjy.util.Debug;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by hjy on 18-1-4.
 * 基于可复用代码实现的Half-sync/Half-async模式客户端
 */
public class SampleAsyncTask {

    public static void main(String[] args) {
        XAsyncTask task = new XAsyncTask();
        List<Future<String>> results = new LinkedList<Future<String>>();

        try {
            results.add(task.doSomething("Half-sync/Half-async",1));

            results.add(task.doSomething("Pattern",2));

            for (Future<String> result :
                    results) {
                Debug.info(result.get());
            }
            Thread.sleep(200);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static class XAsyncTask extends AsyncTask<String>{

        @Override
        protected String doInBackground(Object... params) {
            String message = (String) params[0];
            int sequence = (Integer) params[1];
            Debug.info("doInBackground:"+message);
            return "message "+ sequence + ":" +message;
        }

        @Override
        protected void onPreExecute(Object... params) {
            String message = (String) params[0];
            int sequence = (Integer) params[1];
            Debug.info("onPreExecute:["+sequence+"]"+message);
        }

        public Future<String> doSomething(String message,int sequence){
            if (sequence<0){
                throw new IllegalArgumentException("Invalid sequence:"+sequence);
            }
            return this.dispatch(message,sequence);
        }

    }
}
