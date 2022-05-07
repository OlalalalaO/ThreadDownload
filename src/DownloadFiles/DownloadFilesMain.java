package DownloadFiles;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * 多线程下载多个相同文件
 */
public class DownloadFilesMain {
    public static void main(String[] args) throws IOException, InterruptedException {

        // create a ThreadPool
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(5, 5, 1000L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        String url = "https://down.sandai.net/thunder11/XunLeiWebSetup11.3.9.1902gw.exe";
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            DownloadFilesThread testDown = new DownloadFilesThread(i, url, countDownLatch);
            executorService.execute(testDown);
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("全部下载完成");
    }
}
