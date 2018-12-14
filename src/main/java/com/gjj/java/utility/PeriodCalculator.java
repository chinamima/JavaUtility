package com.gjj.java.utility;


/**
 * Created by guojinjun on 2018/08/27.
 */
public class PeriodCalculator {

    public static final double MS_TO_NS = 1000000;
//    private long mTimeMillisBegin = System.currentTimeMillis();
    private long mTimeNanoBegin = System.nanoTime();

    public void reset() {
        mTimeNanoBegin = System.nanoTime();
    }

    public void printJava(String tag) {
        long timeNanoEnd = System.nanoTime();
        long period = timeNanoEnd - mTimeNanoBegin;
        System.out.printf("%s cost: %.3f ms, %d ns. curr: %d ms\n", tag, period / MS_TO_NS, period, System.currentTimeMillis());
    }

    public void printAndroid(String tag) {
        long timeNanoEnd = System.nanoTime();
        long period = timeNanoEnd - mTimeNanoBegin;
        LogUtil.d(String.format("%s cost: %.3f ms, %d ns. curr: %d ms\n", tag, period / MS_TO_NS, period, System.currentTimeMillis()));
    }
}
