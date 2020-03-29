package org.soldo.concurrency;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * ExecutorService 有三种状态：运行 关闭和终止
 * shutdown 方法将执行平缓的关闭过程：不再接受新的任务，同时等待已经提交的任务执行完成--包括那些还未开始执行的任务
 * shutdownNow 方法将粗暴的关闭过程
 */
public class LifeCycleWebServer {
    private static final int NTHREADS = 100;
    private final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS);

    /**
     * 处理请求方法
     * @param connection socket连接
     */
    private static void handleRequest(Socket connection) {
    }

    public void start() throws IOException {
        ServerSocket socket;
        socket = new ServerSocket(80);
        while (!exec.isShutdown()) {
            try {
                final Socket conn = socket.accept();
                exec.execute(() -> handleRequest(conn));
            }catch (RejectedExecutionException e){
                if(!exec.isShutdown())
                    System.out.println("task submission rejected");
            }
        }
    }

    public void stop(){exec.shutdown();}



}
