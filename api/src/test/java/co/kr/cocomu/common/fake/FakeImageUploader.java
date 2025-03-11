package co.kr.cocomu.common.fake;

import co.kr.cocomu.file.uploader.ImageUploader;
import co.kr.cocomu.file.config.S3Properties;
import co.kr.cocomu.file.factory.S3RequestFactory;
import software.amazon.awssdk.services.s3.S3Client;

public class FakeImageUploader extends ImageUploader {

    public FakeImageUploader(
        final S3RequestFactory s3RequestFactory,
        final S3Properties s3Properties,
        final S3Client s3Client
    ) {
        super(s3RequestFactory, s3Properties, s3Client);
    }

}
