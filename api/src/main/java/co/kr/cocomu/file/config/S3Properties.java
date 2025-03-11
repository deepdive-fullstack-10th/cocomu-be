package co.kr.cocomu.file.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class S3Properties {

    private static final String CONTENT_PREFIX = "contents";

    private final String bucket;
    private final String baseUrl;

    public S3Properties(
        @Value("${spring.cloud.aws.s3.bucket}") final String bucket,
        @Value("${spring.cloud.aws.s3.endpoint}") final String endpoint
    ) {
        this.bucket = bucket;
        this.baseUrl = endpoint;
    }

}
