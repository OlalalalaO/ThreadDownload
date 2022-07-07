package ThreadDownload;

import constant.Constant;
import utils.HttpUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultiThreadDownload {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        System.out.println(args.length);
        if (args.length != 2) {
            System.out.println("请输入完整参数");
            return;
        }
        if (!args[0].matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
            System.out.println("请输入正确的下载地址");
            return;
        }
        if (Integer.parseInt(args[1]) > 64 || Integer.parseInt(args[1]) < 1) {
            System.out.println("请输入正确的线程数(范围1-64)");
        }

        System.out.println("下载地址：" + args[0] + ", 线程数：" + args[1]);
        Constant.DOWNLOAD_URL = args[0];
        Constant.DOWNLOAD_THREAD_NUM = Integer.parseInt(args[1]);
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(Constant.DOWNLOAD_THREAD_NUM + 1, Constant.DOWNLOAD_THREAD_NUM + 1, 1000L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        List<Future<Boolean>> futures = new ArrayList<>();
        HttpURLConnection httpUrlConnection = HttpUtils.getHttpUrlConnection(Constant.DOWNLOAD_URL);
        //get header
        String contentLength = httpUrlConnection.getHeaderField("Content-Length");
        ShowSpeedThread.FILE_SIZE.set(Long.parseLong(contentLength));
        long fileSize = Long.parseLong(contentLength) / 1024 / 1024;
        System.out.println("文件大小：" + fileSize + "MB");
        //create file by file name
        long splitFileSize = Long.parseLong(contentLength) / Constant.DOWNLOAD_THREAD_NUM;
        String filePath = new File("").getCanonicalPath();
        filePath = filePath + "\\download";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        long begin = System.currentTimeMillis();
        //calculate split size
        for (int i = 0; i < Constant.DOWNLOAD_THREAD_NUM; i++) {
            long start = i * splitFileSize;
            long end = (i + 1) * splitFileSize - 1;
            //if it is last file , set end to file size
            if (i == Constant.DOWNLOAD_THREAD_NUM - 1) {
                end = Long.parseLong(contentLength);
            }
            String url = Constant.DOWNLOAD_URL;
            // add thread to thread pool
            futures.add(executorService.submit(new ThreadSplitThread(i, start, end, url, filePath)));
        }
        ShowSpeedThread showSpeedThread = new ShowSpeedThread();
        futures.add(executorService.submit(showSpeedThread));
        for (Future<Boolean> future : futures) {
            future.get();
        }
        executorService.shutdown();
        System.out.println("开始合并文件");
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
        System.out.println("文件合并完成, 总耗时：" + (System.currentTimeMillis() - begin) / 1000 + "秒");
    }

}
