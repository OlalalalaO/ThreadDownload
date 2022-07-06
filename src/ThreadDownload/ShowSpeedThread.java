package ThreadDownload;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

public class ShowSpeedThread implements Callable<Boolean> {
    public static AtomicLong FILE_SIZE = new AtomicLong();
    public static AtomicLong DOWNLOAD_SIZE = new AtomicLong();


    @Override
    public Boolean call() throws Exception {
        while (DOWNLOAD_SIZE.get() < FILE_SIZE.get()) {
            Double percent = DOWNLOAD_SIZE.get() * 1.0 / FILE_SIZE.get();
            String percentStr = String.format("%.2f", percent * 100);
            System.out.print("\r");
            System.out.print("下载进度：" + percentStr + "%");
        }
        return true;
    }
}
