package co.kr.cocomu.file.uploader;

import co.kr.cocomu.common.exception.domain.BadGatewayException;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.file.config.S3Properties;
import co.kr.cocomu.file.exception.FileExceptionCode;
import co.kr.cocomu.file.factory.S3RequestFactory;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectTaggingRequest;

@RequiredArgsConstructor
@Slf4j
public abstract class ImageUploader {

    private static final String CONTENTS_PREFIX = "contents";

    private final S3RequestFactory s3RequestFactory;
    private final S3Properties s3Properties;
    private final S3Client s3Client;

    protected String uploadImage(final MultipartFile file, final String key) {
        final String fullKey = CONTENTS_PREFIX + "/" + key;
        final PutObjectRequest request = s3RequestFactory.createPutRequest(fullKey, file.getContentType());

        try (final InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (final IOException e) {
            throw new BadRequestException(FileExceptionCode.INVALID_FILE_DATA);
        } catch (final AwsServiceException | SdkClientException e) {
            log.error("S3 업로드 실패 - {} : {}", e.getClass(), e.getMessage());
            throw new BadGatewayException(FileExceptionCode.S3_UPLOAD_FAILURE);
        }

        return String.format("%s/%s", s3Properties.getBaseUrl(), key);
    }

    public void confirmImage(final String imageUrl) {
        final String imageKey = extractKeyFromUrl(imageUrl);
        final DeleteObjectTaggingRequest request = s3RequestFactory.createDeleteRequest(imageKey);

        try {
            s3Client.deleteObjectTagging(request);
        } catch (final AwsServiceException | SdkClientException e) {
            log.error("S3 태그 제거 실패 - {} : {}", e.getClass(), e.getMessage());
            throw new BadGatewayException(FileExceptionCode.S3_TAG_REMOVAL_FAILURE);
        }
    }

    public void markAsUnused(final String imageUrl) {
        final String imageKey = extractKeyFromUrl(imageUrl);
        final PutObjectTaggingRequest request = s3RequestFactory.createUnusedTaggingRequest(imageKey);
        try {
            s3Client.putObjectTagging(request);
        } catch (final AwsServiceException | SdkClientException e) {
            log.error("S3 태깅 실패 - {} : {}", e.getClass(), e.getMessage());
            throw new BadGatewayException(FileExceptionCode.S3_TAGGING_FAILURE);
        }
    }

    protected String extractKeyFromUrl(final String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new BadRequestException(FileExceptionCode.INVALID_IMAGE_URL);
        }

        final String relativePath = imageUrl.replace(s3Properties.getBaseUrl() + "/", "");
        return CONTENTS_PREFIX + "/" + relativePath;
    }

}
