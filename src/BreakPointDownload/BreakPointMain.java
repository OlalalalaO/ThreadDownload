package BreakPointDownload;

import constant.Constant;
import utils.HttpUtils;

import java.io.*;
import java.net.HttpURLConnection;

/**
 *
 */
public class BreakPointMain {
    public static void main(String[] args) throws IOException {
        HttpURLConnection httpUrlConnection = HttpUtils.getHttpUrlConnection(Constant.DOWNLOAD_URL);;
        String contentLength = httpUrlConnection.getHeaderField("Content-Length");
        System.out.println("文件大小：" + contentLength);
        String fullFileName = Constant.DOWNLOAD_URL.substring(Constant.DOWNLOAD_URL.lastIndexOf("/") + 1);
        String fileName = fullFileName.substring(0, fullFileName.lastIndexOf("."));
        String filePath = new File("").getCanonicalPath();
        filePath += "\\download";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        filePath += "\\" + fullFileName;
        File downloadFile = new File(filePath);
        if (!downloadFile.exists()) {
            downloadFile.createNewFile();

        }
        if (downloadFile.length() > 0) {
            System.out.println("开始断点续传");
            System.out.println(downloadFile.length());
            httpUrlConnection = HttpUtils.getHttpUrlConnection(Constant.DOWNLOAD_URL, downloadFile.length(), Long.parseLong(contentLength));
            RandomAccessFile rw = new RandomAccessFile(downloadFile, "rw");
            InputStream inputStream = httpUrlConnection.getInputStream();
            rw.seek(downloadFile.length());
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                rw.write(buffer, 0, len);
            }
            rw.close();
            System.out.println("下载完成");
        } else {
            httpUrlConnection = HttpUtils.getHttpUrlConnection(Constant.DOWNLOAD_URL);
            InputStream inputStream = httpUrlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);
            byte[] buffer = new byte[1024];
            int ch = 0;
            while ((ch = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, ch);
            }
            System.out.println("下载完成");
        }
    }


}
