package DownloadFiles;

import utils.HttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;


public class DownloadFilesThread implements Runnable {
    private int index;
    private String url;

    private CountDownLatch latch;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public DownloadFilesThread(int index, String url, CountDownLatch latch) {
        this.index = index;
        this.url = url;
        this.latch = latch;
    }

    @Override
    public void run() {
        String file = url.substring(url.lastIndexOf("/") + 1);
        String fileName = file.substring(0, file.lastIndexOf("."));
        String filePath = "C:\\Users\\11763\\Desktop\\download\\" + fileName + index + ".exe";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        FileOutputStream fileOutputStream = null;
        try {
            urlConnection = HttpUtils.getHttpUrlConnection(url);
            inputStream = urlConnection.getInputStream();
            File savedFile = new File(filePath);
            if (!savedFile.exists()) {
                savedFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(savedFile);
            byte[] buffer = new byte[1024];
            int ch = 0;
            while ((ch = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, ch);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {

                    fileOutputStream.close();
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        latch.countDown();
        System.out.println("下载第" + index + "完成");
    }
}
