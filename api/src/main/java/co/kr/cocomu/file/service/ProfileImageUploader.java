package co.kr.cocomu.file.service;

import co.kr.cocomu.file.ImageUploader;
import co.kr.cocomu.file.config.S3Properties;
import co.kr.cocomu.file.factory.S3RequestFactory;
import co.kr.cocomu.file.parser.MultipartParser;
import co.kr.cocomu.file.validator.MultipartValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class ProfileImageUploader extends ImageUploader {

    private final MultipartValidator multipartValidator;
    private final MultipartParser multipartParser;

    public ProfileImageUploader(
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

    public String uploadProfileImage(final MultipartFile file, final Long userId) {
        multipartValidator.validateImageFile(file);

        final String fileName = multipartParser.parseSaveImageName(file);
        final String key = String.format("images/profiles/%d/%s", userId, fileName);

        return uploadImage(file, key);
    }

}
