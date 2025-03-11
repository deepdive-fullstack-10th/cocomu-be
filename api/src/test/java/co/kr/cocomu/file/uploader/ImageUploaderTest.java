package co.kr.cocomu.file.uploader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.kr.cocomu.common.exception.domain.BadGatewayException;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.fake.FakeImageUploader;
import co.kr.cocomu.file.config.S3Properties;
import co.kr.cocomu.file.exception.FileExceptionCode;
import co.kr.cocomu.file.factory.S3RequestFactory;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectTaggingRequest;

@ExtendWith(MockitoExtension.class)
class ImageUploaderTest {

    private static final String TEST_BASE_URL = "testBaseUrl";

    @Mock private S3RequestFactory s3RequestFactory;
    @Mock private S3Properties s3Properties;
    @Mock private S3Client s3Client;

    private FakeImageUploader imageUploader;

    @BeforeEach
    void setUp() {
        imageUploader = new FakeImageUploader(s3RequestFactory, s3Properties, s3Client);
    }

    @Test
    void 이미지_업로드시_전체_URL을_반환한다() {
        // Given
        MockMultipartFile file = createMultipartFile("test-file", "test.jpg", "image/jpeg", "test".getBytes());
        PutObjectRequest mockRequest = mock(PutObjectRequest.class);
        when(s3Properties.getBaseUrl()).thenReturn(TEST_BASE_URL);
        when(s3RequestFactory.createPutRequest(anyString(), anyString())).thenReturn(mockRequest);

        // when
        String result = imageUploader.uploadImage(file, "images/test.jpg");

        // then
        assertThat(result).isEqualTo(TEST_BASE_URL + "/images/test.jpg");
    }

    @Test
    void 이미지_업로드시_파일_정보를_읽지_못하면_에러가_발생한다() throws IOException {
        // Given
        MockMultipartFile file = mock(MockMultipartFile.class);
        PutObjectRequest mockRequest = mock(PutObjectRequest.class);
        when(s3RequestFactory.createPutRequest(anyString(), anyString())).thenReturn(mockRequest);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getInputStream()).thenThrow(new IOException("Test IO exception"));

        // when & then
        assertThatThrownBy(() -> imageUploader.uploadImage(file, "images/test.jpg"))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.INVALID_FILE_DATA);
    }

    @Test
    void 이미지_업로드시_AWS_Service_예외를_처리한다() {
        // Given
        MockMultipartFile file = createMultipartFile("test-file", "test.jpg", "image/jpeg", "test".getBytes());
        PutObjectRequest mockRequest = mock(PutObjectRequest.class);
        when(s3RequestFactory.createPutRequest(anyString(), anyString())).thenReturn(mockRequest);
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
            .thenThrow(AwsServiceException.builder().build());

        // when & then
        assertThatThrownBy(() -> imageUploader.uploadImage(file, "images/test.jpg"))
            .isInstanceOf(BadGatewayException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.S3_UPLOAD_FAILURE);
    }

    @Test
    void 이미지_업로드시_SDK_Client_예외를_처리한다() {
        // Given
        MockMultipartFile file = createMultipartFile("test-file", "test.jpg", "image/jpeg", "test".getBytes());
        PutObjectRequest mockRequest = mock(PutObjectRequest.class);
        when(s3RequestFactory.createPutRequest(anyString(), anyString())).thenReturn(mockRequest);
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
            .thenThrow(SdkClientException.builder().build());

        // when & then
        assertThatThrownBy(() -> imageUploader.uploadImage(file, "images/test.jpg"))
            .isInstanceOf(BadGatewayException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.S3_UPLOAD_FAILURE);
    }

    @Test
    void 이미지_사용을_확정한다() {
        // given
        String imageUrl = "testUrl";
        DeleteObjectTaggingRequest mockReq = mock(DeleteObjectTaggingRequest.class);
        when(s3RequestFactory.createDeleteRequest(anyString())).thenReturn(mockReq);

        // when
        imageUploader.confirmImage(imageUrl);

        // then
        verify(s3Client).deleteObjectTagging(mockReq);
    }

    @Test
    void 이미지_사용_확정_시_SdkClient_예외를_처리한다() {
        // given
        String imageUrl = "testUrl";
        DeleteObjectTaggingRequest mockReq = mock(DeleteObjectTaggingRequest.class);
        when(s3RequestFactory.createDeleteRequest(anyString())).thenReturn(mockReq);
        when(s3Client.deleteObjectTagging(mockReq)).thenThrow(SdkClientException.builder().build());

        // when & then
        assertThatThrownBy(() -> imageUploader.confirmImage(imageUrl))
            .isInstanceOf(BadGatewayException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.S3_TAG_REMOVAL_FAILURE);
    }

    @Test
    void 이미지_사용_확정_시_AwsService_예외를_처리한다() {
        // given
        String imageUrl = "testUrl";
        DeleteObjectTaggingRequest mockReq = mock(DeleteObjectTaggingRequest.class);
        when(s3RequestFactory.createDeleteRequest(anyString())).thenReturn(mockReq);
        when(s3Client.deleteObjectTagging(mockReq)).thenThrow(AwsServiceException.builder().build());

        // when & then
        assertThatThrownBy(() -> imageUploader.confirmImage(imageUrl))
            .isInstanceOf(BadGatewayException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.S3_TAG_REMOVAL_FAILURE);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이미지_사용_확정_시_이미지URL이_없을_때_예외를_처리한다(String imageUrl) {
        // given
        // when & then
        assertThatThrownBy(() -> imageUploader.confirmImage(imageUrl))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.INVALID_IMAGE_URL);
    }

    @Test
    void 사용하지_않는_이미지에_태그를_할당한다() {
        // given
        String imageUrl = "testUrl";
        PutObjectTaggingRequest mockReq = mock(PutObjectTaggingRequest.class);
        when(s3RequestFactory.createUnusedTaggingRequest(anyString())).thenReturn(mockReq);

        // when
        imageUploader.markAsUnused(imageUrl);

        // then
        verify(s3Client).putObjectTagging(mockReq);
    }

    @Test
    void 사용하지_않는_이미지에_태그를_할당중_AwsService_예외를_처리한다() {
        // given
        String imageUrl = "testUrl";
        PutObjectTaggingRequest mockReq = mock(PutObjectTaggingRequest.class);
        when(s3RequestFactory.createUnusedTaggingRequest(anyString())).thenReturn(mockReq);
        when(s3Client.putObjectTagging(mockReq)).thenThrow(AwsServiceException.builder().build());

        // when & then
        assertThatThrownBy(() -> imageUploader.markAsUnused(imageUrl))
            .isInstanceOf(BadGatewayException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.S3_TAGGING_FAILURE);
    }

    @Test
    void 사용하지_않는_이미지에_태그를_할당중_SdkClient_예외를_처리한다() {
        // given
        String imageUrl = "testUrl";
        PutObjectTaggingRequest mockReq = mock(PutObjectTaggingRequest.class);
        when(s3RequestFactory.createUnusedTaggingRequest(anyString())).thenReturn(mockReq);
        when(s3Client.putObjectTagging(mockReq)).thenThrow(SdkClientException.builder().build());

        // when & then
        assertThatThrownBy(() -> imageUploader.markAsUnused(imageUrl))
            .isInstanceOf(BadGatewayException.class)
            .hasFieldOrPropertyWithValue("exceptionType", FileExceptionCode.S3_TAGGING_FAILURE);
    }

    private static @NotNull MockMultipartFile createMultipartFile(
        final String name, final String original, final String contentType, final byte[] bytes
    ) {
        return new MockMultipartFile(name, original, contentType, bytes);
    }

}