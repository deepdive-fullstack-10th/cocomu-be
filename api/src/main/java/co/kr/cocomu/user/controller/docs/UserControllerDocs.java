package co.kr.cocomu.user.controller.docs;

import co.kr.cocomu.codingspace.dto.response.CodingSpaceDto;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.api.NoContent;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import co.kr.cocomu.study.dto.response.StudyCardDto;
import co.kr.cocomu.user.dto.request.ProfileUpdateDto;
import co.kr.cocomu.user.dto.response.UserInfoDto;
import co.kr.cocomu.user.dto.response.UserResponse;
import co.kr.cocomu.user.dto.request.UserJoinRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "001. COCOMU-USER", description = "코코무 사용자 관련 API")
public interface UserControllerDocs {

    @Operation(summary = "프로필 조회", description = "프로필을 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "사용자 정보 조회에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "404",
        description = "사용자를 찾을 수 없습니다.",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<UserResponse> getProfile(Long userId);

    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "사용자 정보 조회에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "404",
        description = "사용자를 찾을 수 없습니다.",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<UserInfoDto> getUserInformation(Long authUserId, Long userId);

    @Operation(summary = "모든 사용자 정보 조회", description = "모든 사용자 정보를 조회하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "모든 사용자 정보를 조회했습니다."
    )
    Api<List<UserResponse>> getUsers();

    @Operation(summary = "사용자 정보 추가", description = "사용자 정보를 추가하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "사용자 정보가 추가되었습니다."
    )
    Api<UserResponse> createUser(UserJoinRequest userId);

    @Operation(summary = "프로필 정보 수정", description = "프로필 정보를 수정하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "프로필 정보 수정이 성공했습니다."
    )
    @ApiResponse(
        responseCode = "502",
        description = """
            파일 태그 설정에 실패했습니다.
            파일 태그 제거에 실패했습니다.
            사용자 닉네임 정보가 잘못되었습니다.
        """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    NoContent updateUser(Long userId, ProfileUpdateDto dto);

    @Operation(summary = "프로필 이미지 업로드", description = "프로필 이미지를 업로드하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "프로필 이미지 업로드가 성공했습니다."
    )
    @ApiResponse(
        responseCode = "400",
        description = """
            이미지 파일이 아닙니다.
            잘못된 파일 정보입니다.
        """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = """
            파일을 찾을 수 없습니다.
            파일 이름 정보가 없습니다.
        """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
        responseCode = "502",
        description = """
            파일 업로드에 실패했습니다.
        """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<String> uploadProfileImage(MultipartFile image, Long userId);

    @Operation(summary = "참여한 스터디 조회", description = "코코무 회원의 참여한 스터디 목록을 조회한다")
    @ApiResponse(
        responseCode = "200",
        description = "참여한 스터디 목록 조회에 성공했습니다."
    )
    Api<List<StudyCardDto>> getStudyCards(Long userId, Long viewerId, Long lastIndex);

    @Operation(summary = "참여한 코딩 스페이스 조회", description = "코코무 회원의 참여한 스페이스 목록을 조회한다")
    @ApiResponse(
        responseCode = "200",
        description = "참여한 코딩 스페이스 목록 조회에 성공했습니다."
    )
    Api<List<CodingSpaceDto>> getCodingSpaces(Long userId, Long viewerId, Long lastIndex);

}
