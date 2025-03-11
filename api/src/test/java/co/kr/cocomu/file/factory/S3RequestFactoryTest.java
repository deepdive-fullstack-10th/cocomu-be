package co.kr.cocomu.file.factory;

import co.kr.cocomu.file.config.S3Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.model.DeleteObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.Tag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3RequestFactoryTest {

    @Mock private S3Properties s3Properties;
    @InjectMocks private S3RequestFactory s3RequestFactory;

    @BeforeEach
    void setUp() {
        when(s3Properties.getBucket()).thenReturn("test-bucket");
    }

    @Test
    void createPutRequest_S3_업로드_요청을_생성한다() {
        // given
        String key = "test/image.jpg";
        String contentType = "image/jpeg";

        // when
        PutObjectRequest result = s3RequestFactory.createPutRequest(key, contentType);

        // then
        assertThat(result.bucket()).isEqualTo("test-bucket");
        assertThat(result.key()).isEqualTo(key);
        assertThat(result.contentType()).isEqualTo(contentType);
        assertThat(result.tagging()).isEqualTo("status=temporary");
    }

    @Test
    void createDeleteRequest_태그_삭제_요청을_생성한다() {
        // given
        String key = "test/image.jpg";

        // when
        DeleteObjectTaggingRequest result = s3RequestFactory.createDeleteRequest(key);

        // then
        assertThat(result.bucket()).isEqualTo("test-bucket");
        assertThat(result.key()).isEqualTo(key);
    }

    @Test
    void createUnusedTaggingRequest_미사용_태그_추가_요청을_생성한다() {
        // given
        String key = "test/image.jpg";

        // when
        PutObjectTaggingRequest result = s3RequestFactory.createUnusedTaggingRequest(key);

        // then
        assertThat(result.bucket()).isEqualTo("test-bucket");
        assertThat(result.key()).isEqualTo(key);

        // 태깅 검증
        assertThat(result.tagging().tagSet()).hasSize(1);
        Tag tag = result.tagging().tagSet().get(0);
        assertThat(tag.key()).isEqualTo("status");
        assertThat(tag.value()).isEqualTo("unused");
    }
}