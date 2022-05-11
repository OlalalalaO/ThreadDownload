package ThreadDownload;

import constant.Constant;
import utils.HttpUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;

public class ThreadSplit implements Runnable {
    private final int index;
    private long start;
    private final long end;
    private final String url;
    private String filePath;
    private final CountDownLatch countDownLatch;

    public ThreadSplit(int index, long start, long end, String url, String filePath, CountDownLatch countDownLatch) {
        this.index = index;
        this.start = start;
        this.end = end;
        this.url = url;
        this.filePath = filePath;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        HttpURLConnection httpUrlConnection = null;
        InputStream inputStream = null;
        RandomAccessFile raf = null;
        try {
            String fullFileName = Constant.DOWNLOAD_URL.substring(Constant.DOWNLOAD_URL.lastIndexOf("/") + 1);
            filePath += "\\" + "temp_" + index + "_" + fullFileName;
            File downloadFile = new File(filePath);
            Long offset = 0L;
            if (!downloadFile.exists()) {
                downloadFile.createNewFile();
            } else {
                System.out.println("第" + index + "个文件大小" + downloadFile.length() + "--------" + "下载大小" + (end - start));
                if (downloadFile.length() == end - start) {
                    System.out.println("线程" + index + "已完成");
                    return;
                }
                System.out.println("断点续传文件" + index);
                offset = downloadFile.length();
            }
            start = start + offset;
            httpUrlConnection = HttpUtils.getHttpUrlConnection(url, start, end);
            inputStream = httpUrlConnection.getInputStream();
            raf = new RandomAccessFile(downloadFile, "rw");
            raf.seek(offset);
            byte[] buffer = new byte[1024];
            int ch = 0;
            while ((ch = inputStream.read(buffer)) != -1) {
                raf.write(buffer, 0, ch);
            }
            raf.close();
            inputStream.close();
            httpUrlConnection.disconnect();
        } catch (IOException e) {
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (raf != null) {
                    raf.close();
                }
                if (httpUrlConnection != null) {
                    httpUrlConnection.disconnect();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            countDownLatch.countDown();
        }
        System.out.println("文件" + index + "下载完成");
    }

}
