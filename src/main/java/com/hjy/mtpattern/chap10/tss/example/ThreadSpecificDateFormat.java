package com.hjy.mtpattern.chap10.tss.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hjy on 17-12-8.
 */
public class ThreadSpecificDateFormat {

    private static final ThreadLocal<SimpleDateFormat> TS_SDF = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };

    public static Date parse(String timeStamp,String format) throws ParseException{
        final SimpleDateFormat sdf = TS_SDF.get();
        sdf.applyPattern(format);
        Date date = sdf.parse(timeStamp);
        return date;
    }

    public static void main(String[] args) throws Exception{
        Date date = ThreadSpecificDateFormat.parse("20171208143330","yyyyMMddHHmmss");
        System.out.println(date);
    }






}
