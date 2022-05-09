package utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static HttpURLConnection getHttpUrlConnection(String url) throws IOException {
        URL httpUrl = new URL(url);
        return (HttpURLConnection) httpUrl.openConnection();
    }


    public static HttpURLConnection getHttpUrlConnection(String url, Long start, Long end) throws IOException {
        HttpURLConnection httpUrlConnection = getHttpUrlConnection(url);
        httpUrlConnection.setRequestProperty("Range", "bytes=" + start + "-" + end);
        return httpUrlConnection;
    }
}
