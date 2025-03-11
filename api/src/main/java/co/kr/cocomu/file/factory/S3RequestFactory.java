package co.kr.cocomu.file.factory;

import co.kr.cocomu.file.config.S3Properties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.DeleteObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.s3.model.Tagging;

@Component
@RequiredArgsConstructor
public class S3RequestFactory {

    private static final String TEMPORARY_TAG = "status=temporary";
    private static final String UNUSED_TAG_KEY = "status";
    private static final String UNUSED_TAG_VALUE = "unused";

    private final S3Properties s3Properties;

    public PutObjectRequest createPutRequest(final String key, final String contentType) {
        return PutObjectRequest.builder()
            .bucket(s3Properties.getBucket())
            .key(key)
            .contentType(contentType)
            .tagging(TEMPORARY_TAG)
            .build();
    }

    public DeleteObjectTaggingRequest createDeleteRequest(final String key) {
        return DeleteObjectTaggingRequest.builder()
            .bucket(s3Properties.getBucket())
            .key(key)
            .build();
    }

    public PutObjectTaggingRequest createUnusedTaggingRequest(final String key) {
        final Tagging tagging = createTagging(UNUSED_TAG_KEY, UNUSED_TAG_VALUE);

        return PutObjectTaggingRequest.builder()
            .bucket(s3Properties.getBucket())
            .key(key)
            .tagging(tagging)
            .build();
    }

    private static Tagging createTagging(final String key, final String value) {
        final Tag tag = createTag(key, value);

        return Tagging.builder()
            .tagSet(List.of(tag))
            .build();
    }

    private static Tag createTag(final String key, final String value) {
        return Tag.builder()
            .key(key)
            .value(value)
            .build();
    }

}
