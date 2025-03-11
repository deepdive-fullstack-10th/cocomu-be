package co.kr.cocomu.file.exception;

import co.kr.cocomu.common.exception.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileExceptionCode implements ExceptionCode {

    NOT_FOUND_FILE_NAME(6001, "파일 이름 정보가 없습니다."),
    S3_UPLOAD_FAILURE(6002, "파일 업로드에 실패했습니다."),
    NOT_FOUND_FILE(6003, "파일을 찾을 수 없습니다."),
    INVALID_IMAGE_FILE(6004, "이미지 파일이 아닙니다."),
    S3_TAG_REMOVAL_FAILURE(6005, "파일 태그 제거에 실패했습니다."),
    INVALID_IMAGE_URL(6006, "잘못된 Image Url입니다."),
    S3_TAGGING_FAILURE(6007, "파일 태그 설정에 실패했습니다."),
    INVALID_FILE_DATA(6008, "잘못된 파일 정보입니다.");
    
    private final int code;
    private final String message;
    
}
