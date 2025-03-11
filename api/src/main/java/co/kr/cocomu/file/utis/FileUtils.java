package co.kr.cocomu.file.utis;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class FileUtils {

    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public String generateUniqueId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
