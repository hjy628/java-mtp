package com.hjy.mtpattern.chap10.tss.example.memoryleak;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hjy on 17-12-8.
 */
public class MemoryPseduoLeakingServlet extends HttpServlet{
    private static final long serialVersionUID = 2392875982786576616L;

    private final static ThreadLocal<AtomicInteger> TL_COUNTER = new ThreadLocal<AtomicInteger>(){
        @Override
        protected AtomicInteger initialValue() {
            return new AtomicInteger();
        }
    };

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.write(String.valueOf(TL_COUNTER.get().getAndIncrement()));
        writer.close();
    }
}
