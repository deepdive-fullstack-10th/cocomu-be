package co.kr.cocomu.file.parser;

import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.file.exception.FileExceptionCode;
import co.kr.cocomu.file.utis.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MultipartParserTest {

    @Mock
    private FileUtils fileUtils;

    @InjectMocks
    private MultipartParser multipartParser;

    @Test
    void 유효한_이미지_파일의_이름을_성공적으로_생성한다() {
        // given
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test".getBytes());

        when(fileUtils.generateUniqueId()).thenReturn("file");
        when(fileUtils.getCurrentTimeMillis()).thenReturn(1234L);

        // when
        String result = multipartParser.parseSaveImageName(file);

        // then
        assertThat(result).isEqualTo("file_1234.jpg");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 원본_파일명이_null또는_Empty이면_예외를_발생시킨다(String originalFileName) {
        // given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(originalFileName);

        // when & then
        assertThatThrownBy(() -> multipartParser.parseSaveImageName(file))
            .isInstanceOf(NotFoundException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.NOT_FOUND_FILE_NAME);
    }

    @Test
    void 허용되지_않은_확장자_파일이면_예외를_발생시킨다() {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "image/png", "test".getBytes());

        // when & then
        assertThatThrownBy(() -> multipartParser.parseSaveImageName(file))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.INVALID_IMAGE_FILE);
    }

    @Test
    void 대문자_확장자도_허용된다() {
        // given
        MockMultipartFile file = new MockMultipartFile("image", "test.JPG", "image/jpeg", "test".getBytes());

        when(fileUtils.generateUniqueId()).thenReturn("file");
        when(fileUtils.getCurrentTimeMillis()).thenReturn(1234L);

        // when
        String result = multipartParser.parseSaveImageName(file);

        // then
        assertThat(result).isEqualTo("file_1234.jpg");
    }

}