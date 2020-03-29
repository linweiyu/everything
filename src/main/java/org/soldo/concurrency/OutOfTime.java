package org.soldo.concurrency;

import java.util.Timer;
import java.util.TimerTask;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Timer 缺陷示例
 * 当执行到一些可能抛出Timer无法解决的异常时 Timer会停止工作
 */
public class OutOfTime {
    public static void main(String[] args) throws Exception{
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(),1);
        SECONDS.sleep(5);
        timer.schedule(new ThrowTask(), 5);
        SECONDS.sleep(1);
    }

    static class ThrowTask extends TimerTask{
        @Override
        public void run() {
            throw new RuntimeException();
        }
    }


}
