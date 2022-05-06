package utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static HttpURLConnection getHttpUrlConnection(String url) throws IOException {
        URL httpUrl = new URL(url);
        return (HttpURLConnection) httpUrl.openConnection();
    }

}
