package co.kr.cocomu.file.utis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FileUtilsTest {

    private final FileUtils fileUtils = new FileUtils();

    @Test
    void getCurrentTimeMillis_현재_시간을_반환한다() {
        // given
        long beforeTestTime = System.currentTimeMillis();

        // when
        long result = fileUtils.getCurrentTimeMillis();

        // then
        assertThat(result).isGreaterThanOrEqualTo(beforeTestTime);
    }

    @Test
    void generateUniqueId_8자리_고유_문자열을_생성한다() {
        // when
        String result = fileUtils.generateUniqueId();

        // then
        assertThat(result).hasSize(8);
    }

}