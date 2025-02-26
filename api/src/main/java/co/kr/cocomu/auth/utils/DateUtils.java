package co.kr.cocomu.auth.utils;

import java.util.Date;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    public static Date getDate() {
        return new Date();
    }

    public static Date getDate(long dateTime) {
        return new Date(dateTime);
    }

}
