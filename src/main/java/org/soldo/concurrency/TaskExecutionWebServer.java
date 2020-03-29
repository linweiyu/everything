package org.soldo.concurrency;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 基于 Executor 的 web 服务器
 * 可自定义 Executor 的实现类来扩展Executor的功能
 */
public class TaskExecutionWebServer {
    private static final int NTHREADS = 100;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);

    /**
     * 处理请求方法
     *
     * @param connection socket连接
     */
    private static void handleRequest(Socket connection) {
    }

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            Runnable task;
            try (Socket connection = socket.accept()) {
                task = () -> handleRequest(connection);
            }
            exec.execute(task);
        }
    }
}
