package utils;

import org.w3c.dom.ranges.Range;

public class DownloadUtils {
    public static String parameterVerification(String[] args) {
        if (args.length != 2) {
            return "请输入完整参数";
        }
        if (!args[0].matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
            return "请输入正确的下载地址";
        }
        if (Integer.parseInt(args[1]) > 64 || Integer.parseInt(args[1]) < 1) {
            System.out.println();
            return "请输入正确的线程数(范围1-64)";
        }
        return "success";
    }



}
