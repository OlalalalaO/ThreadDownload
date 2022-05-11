package ThreadDownload;

import constant.Constant;
import utils.HttpUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadDownload {

    //creat a ThreadPool
    private static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(Constant.DOWNLOAD_THREAD_NUM, Constant.DOWNLOAD_THREAD_NUM, 1000L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());


    public static void main(String[] args) throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(Constant.DOWNLOAD_THREAD_NUM);
        HttpURLConnection httpUrlConnection = HttpUtils.getHttpUrlConnection(Constant.DOWNLOAD_URL);
        String contentLength = httpUrlConnection.getHeaderField("Content-Length");
        System.out.println("文件大小：" + contentLength);
        long splitFileSize = Long.parseLong(contentLength) / Constant.DOWNLOAD_THREAD_NUM;
        String filePath = new File("").getCanonicalPath();
        filePath = filePath + "\\download";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (int i = 0; i < Constant.DOWNLOAD_THREAD_NUM; i++) {
            long start = i * splitFileSize;
            long end = (i + 1) * splitFileSize - 1;
            if (i == Constant.DOWNLOAD_THREAD_NUM - 1) {
                end = Long.parseLong(contentLength);
            }
            String url = Constant.DOWNLOAD_URL;
            ThreadSplit threadSplit = new ThreadSplit(i, start, end, url, filePath, countDownLatch);
            executorService.submit(threadSplit);
        }
        countDownLatch.await();
        executorService.shutdown();
        String firstFilePath = filePath + "\\" + Constant.DOWNLOAD_URL.substring(Constant.DOWNLOAD_URL.lastIndexOf("/") + 1);
        File mergeFile = new File(firstFilePath);
        mergeFile.createNewFile();
        RandomAccessFile firstFile = new RandomAccessFile(firstFilePath, "rw");
        for (int i = 0; i < Constant.DOWNLOAD_THREAD_NUM; i++) {
            String tempFilePath = filePath + "\\temp_" + i + "_" + Constant.DOWNLOAD_URL.substring(Constant.DOWNLOAD_URL.lastIndexOf("/") + 1);
            FileInputStream fis = new FileInputStream(tempFilePath);
            byte[] buffer = new byte[1024];
            int ch = 0;
            while ((ch = fis.read(buffer)) != -1) {
                firstFile.write(buffer, 0, ch);
            }
            fis.close();
            File tempFile = new File(tempFilePath);
            tempFile.delete();
        }
        System.out.println("文件合并完成");
    }

}
