package com.hjy.mtpattern.chap11.stc.example;

import com.hjy.mtpattern.chap11.stc.AbstractSerializer;
import com.hjy.mtpattern.chap11.stc.TaskProcessor;

import java.sql.Time;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 17-12-8.
 * 使用可复用代码实现Serial Thread Confiment模式示例代码
 */
public class ReusableCodeExample {

    public static void main(String[] args) throws InterruptedException ,ExecutionException{
        SomeService ss = new SomeService();
        ss.init();
        Future<String> result = ss.doSomething("Serial Thread Confinment",1);

        TimeUnit.SECONDS.sleep(1);

        System.out.println(result.get());

        ss.shutdown();

    }



    private static class Task{
        public final String message;
        public final int id;

        public Task(String message, int id) {
            this.message = message;
            this.id = id;
        }
    }

    private static class SomeService extends AbstractSerializer<Task,String>{

        public SomeService(){
            super(new ArrayBlockingQueue<Runnable>(100),
                    new TaskProcessor<Task, String>() {
                        @Override
                        public String doProcess(Task task) throws Exception {
                            System.out.println("["+task.id+"]"+task.message);
                            return task.message+" accepted.";
                        }
                    });
        }

        @Override
        protected Task makeTask(Object... params) {
            String message = (String)params[0];
            int id = (Integer)params[1];
            return new Task(message,id);
        }

        public Future<String> doSomething(String message,int id)throws InterruptedException{
            Future<String> result = null;

            result = service(message,id);

            return result;
        }

    }




}
