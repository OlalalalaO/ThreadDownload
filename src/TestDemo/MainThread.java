package TestDemo;

import utils.HttpUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.*;

public class MainThread {
    public static void main(String[] args) throws IOException, InterruptedException {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(5, 5, 1000L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        String url = "https://down.sandai.net/thunder11/XunLeiWebSetup11.3.9.1902gw.exe";


        CountDownLatch countDownLatch = new CountDownLatch(5);
        //Map<String, String> headers = HttpUtils.getHeaders(httpUrlConnection);
        //Integer contentLength = Integer.getInteger(headers.get("Content-Length"));
        for (int i = 0; i < 5; i++) {
            TestDown testDown = new TestDown(i, url, countDownLatch);
            executorService.execute(testDown);
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("全部下载完成");
    }
}
