package co.kr.cocomu.admin.uploader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.kr.cocomu.file.config.S3Properties;
import co.kr.cocomu.file.factory.S3RequestFactory;
import co.kr.cocomu.file.parser.MultipartParser;
import co.kr.cocomu.file.validator.MultipartValidator;
import co.kr.cocomu.user.uploader.ProfileImageUploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

@ExtendWith(MockitoExtension.class)
class AdminImageUploaderTest {

    @Mock private S3RequestFactory s3RequestFactory;
    @Mock private S3Properties s3Properties;
    @Mock private S3Client s3Client;
    @Mock private MultipartValidator multipartValidator;
    @Mock private MultipartParser multipartParser;

    private AdminImageUploader adminImageUploader;

    @BeforeEach
    void setUp() {
        adminImageUploader = new AdminImageUploader(
            s3RequestFactory, s3Properties, s3Client, multipartValidator, multipartParser
        ) {
            @Override
            protected String uploadImage(MultipartFile file, String key) {
                return "imageUrl";
            }
        };
    }

    @Test
    void 공용_이미지_업로드가_성공적으로_진행된다() {
        // given
        MultipartFile file = mock(MultipartFile.class);
        doNothing().when(multipartValidator).validateImageFile(file);
        when(multipartParser.parseSaveImageName(file)).thenReturn("testImageName");

        // when
        String result = adminImageUploader.uploadCommonImage(file);

        // then
        assertThat(result).isEqualTo("imageUrl");
    }

}