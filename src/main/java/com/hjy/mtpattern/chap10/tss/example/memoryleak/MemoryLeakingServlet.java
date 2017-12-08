package com.hjy.mtpattern.chap10.tss.example.memoryleak;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hjy on 17-12-8.
 */
public class MemoryLeakingServlet extends HttpServlet{


    private static final long serialVersionUID = -4957333198430889007L;

    private final static ThreadLocal<Counter> TL_COUNTER = new ThreadLocal<Counter>(){
        @Override
        protected Counter initialValue() {
            return new Counter();
        }
    };

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.write(String.valueOf(TL_COUNTER.get().getAndIncrement()));
        writer.close();
    }
}
