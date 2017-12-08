package com.hjy.mtpattern.chap10.tss.example.memoryleak;

import com.hjy.mtpattern.chap10.tss.ManagedThreadLocal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hjy on 17-12-8.
 */
public class MemoryLeakPreventingServlet extends HttpServlet{
    private static final long serialVersionUID = -1123876343739290244L;

    private final static ManagedThreadLocal<Counter> TL_COUNTER = ManagedThreadLocal
            .newInstance(new ManagedThreadLocal.InitialValueProvider<Counter>(){
                @Override
                protected Counter initialValue() {
                    return new Counter();
                }
            });

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.write(String.valueOf(TL_COUNTER.get().getAndIncrement()));
        writer.close();
    }

    @Override
    public void destroy() {
        TL_COUNTER.removeALl();
        super.destroy();
    }
}
