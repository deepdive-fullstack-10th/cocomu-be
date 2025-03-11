package co.kr.cocomu.admin.uploader;

import co.kr.cocomu.file.config.S3Properties;
import co.kr.cocomu.file.factory.S3RequestFactory;
import co.kr.cocomu.file.parser.MultipartParser;
import co.kr.cocomu.file.uploader.ImageUploader;
import co.kr.cocomu.file.validator.MultipartValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

@Component
public class AdminImageUploader extends ImageUploader {

    private static final String IMAGE_KEY_FORMAT = "common/%s";

    private final MultipartValidator multipartValidator;
    private final MultipartParser multipartParser;

    public AdminImageUploader(
        final S3RequestFactory s3RequestFactory,
        final S3Properties s3Properties,
        final S3Client s3Client,
        final MultipartValidator multipartValidator,
        final MultipartParser multipartParser
    ) {
        super(s3RequestFactory, s3Properties, s3Client);
        this.multipartValidator = multipartValidator;
        this.multipartParser = multipartParser;
    }

    public String uploadCommonImage(final MultipartFile file) {
        multipartValidator.validateImageFile(file);

        final String fileName = multipartParser.parseSaveImageName(file);
        final String key = String.format(IMAGE_KEY_FORMAT, fileName);

        return uploadImage(file, key);
    }

}