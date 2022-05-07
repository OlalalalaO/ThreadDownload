package DownloadFile;

import utils.HttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * time: 2020/7/15 15:15
 * author: olalalao
 * 下载一个文件
 */
public class MainDownload {
    public static void main(String[] args) throws IOException {
        HttpURLConnection httpUrlConnection = HttpUtils.getHttpUrlConnection("https://download-cdn.jetbrains.com/idea/ideaIU-2022.1.exe");
        Map<String, List<String>> headerFields = httpUrlConnection.getHeaderFields();
        InputStream inputStream = httpUrlConnection.getInputStream();
        String filePath = "C:\\Users\\11763\\Desktop\\download\\test.exe";
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int ch = 0;
        while ((ch = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, ch);
        }
        System.out.println("下载完成");
    }
}
