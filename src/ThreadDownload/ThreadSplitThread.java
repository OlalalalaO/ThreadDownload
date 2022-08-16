package ThreadDownload;

import constant.Constant;
import utils.HttpUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

public class ThreadSplitThread implements Callable<Boolean> {
    private final int index;
    private long start;
    private final long end;
    private final String url;
    private String filePath;

    public ThreadSplitThread(int index, long start, long end, String url, String filePath) {
        this.index = index;
        this.start = start;
        this.end = end;
        this.url = url;
        this.filePath = filePath;
    }


    @Override
    public Boolean call() throws Exception {
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
                if (downloadFile.length() == end - start + 1) {
                    ShowSpeedThread.DOWNLOAD_SIZE.addAndGet(downloadFile.length());
                    return true;
                }
                offset = downloadFile.length();
                ShowSpeedThread.DOWNLOAD_SIZE.addAndGet(offset);
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
                ShowSpeedThread.DOWNLOAD_SIZE.addAndGet(ch);
            }
            raf.close();
            inputStream.close();
            httpUrlConnection.disconnect();
        } catch (IOException ignored) {
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
        }
        return true;
    }
}
