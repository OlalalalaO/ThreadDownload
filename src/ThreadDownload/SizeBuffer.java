package ThreadDownload;

import java.util.ArrayList;
import java.util.List;

// 平均下载速度
public class SizeBuffer {
    private static final List<Long> SIZE_BUFFER = new ArrayList<>(5);

    public static Long push(long size) {
        if (SIZE_BUFFER.size() == 5) {
            SIZE_BUFFER.remove(0);
            SIZE_BUFFER.add(size);
        } else {
            SIZE_BUFFER.add(size);
        }
        return SIZE_BUFFER.stream().mapToLong(Long::longValue).sum() / SIZE_BUFFER.size();
    }


}
