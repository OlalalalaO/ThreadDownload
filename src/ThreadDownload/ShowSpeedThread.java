package ThreadDownload;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

public class ShowSpeedThread implements Callable<Boolean> {
    public static AtomicLong FILE_SIZE = new AtomicLong();
    public static AtomicLong DOWNLOAD_SIZE = new AtomicLong();


    @Override
    public Boolean call() throws Exception {
        Long alreadyDownloadSize = 0L;
        while (DOWNLOAD_SIZE.get() < FILE_SIZE.get()) {
            Long downloadSize = DOWNLOAD_SIZE.get();
            Double downloadPercent = downloadSize * 1.0 / FILE_SIZE.get();
            String percentStr = String.format("%.2f", downloadPercent * 100);
            System.out.print("\r");
            System.out.print("下载速度：" + SizeBuffer.push(downloadSize - alreadyDownloadSize) / 1024 + "KB/s, " + "下载进度：" + percentStr + "%");
            alreadyDownloadSize = DOWNLOAD_SIZE.get();
            Thread.sleep(1000);
        }
        return true;
    }
}
