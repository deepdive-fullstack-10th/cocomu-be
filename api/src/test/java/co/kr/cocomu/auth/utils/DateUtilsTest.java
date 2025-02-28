package co.kr.cocomu.auth.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

    @Test
    void 새로운_Date객체를_가져온다() {
        Date date1 = DateUtils.getDate();
        Date date2 = DateUtils.getDate();
        assertThat(date1).isNotSameAs(date2);
    }

    @Test
    void 특정_시간_별_Date객체를_가져온다() {
        Date date1 = DateUtils.getDate();
        Date date2 = DateUtils.getDate(date1.getTime() + 1000);
        assertThat(date1).isBefore(date2);
    }

}