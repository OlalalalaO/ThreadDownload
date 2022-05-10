package ThreadDownload;

import constant.Constant;
import utils.HttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;

public class ThreadSplit implements Runnable {
    private int index;
    private long start;
    private long end;
    private String url;
    private String filePath;

    private CountDownLatch countDownLatch;

    public ThreadSplit(int index, long start, long end, String url, String filePath,CountDownLatch countDownLatch) {
        this.index = index;
        this.start = start;
        this.end = end;
        this.url = url;
        this.filePath = filePath;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            String fullFileName = Constant.DOWNLOAD_URL.substring(Constant.DOWNLOAD_URL.lastIndexOf("/") + 1);
            HttpURLConnection httpUrlConnection = HttpUtils.getHttpUrlConnection(url, start, end);
            InputStream inputStream = httpUrlConnection.getInputStream();
            filePath += "\\" + "temp_" + index + "_" + fullFileName;
            File downloadFile = new File(filePath);
            if (!downloadFile.exists()) {
                downloadFile.createNewFile();

            }
            FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);
            byte[] buffer = new byte[1024];
            int ch = 0;
            while ((ch = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, ch);
            }
            fileOutputStream.close();
        } catch (IOException e) {
        }finally {
            countDownLatch.countDown();
        }
        System.out.println("文件" + index + "下载完成");
    }

}
