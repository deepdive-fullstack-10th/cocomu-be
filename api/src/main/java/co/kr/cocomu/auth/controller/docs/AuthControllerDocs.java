package co.kr.cocomu.auth.controller.docs;

import co.kr.cocomu.auth.dto.request.OAuthRequest;
import co.kr.cocomu.auth.dto.response.AuthResponse;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "002. COCOMU-USER-AUTH", description = "코코무 사용자 인증 관련 API")
public interface AuthControllerDocs {

    @Operation(summary = "백도어 로그인", description = "개발용으로 임시 로그인을 하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "로그인에 성공했습니다."
    )
    Api<AuthResponse> login(HttpServletResponse response);

    @Operation(summary = "OAuth 로그인", description = "OAuth 로그인을 하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "로그인에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "400",
        description = "제공되지 않는 OAuth Service 입니다.",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
        responseCode = "502",
        description = """
            OAuth 로그인에 실패했습니다.
            OAuth 로그인 중 알 수 없는 에러가 발생했습니다.
        """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    Api<AuthResponse> loginWithOAuth2(OAuthRequest request, HttpServletResponse response);

    @Operation(summary = "OAuth 개발용 로그인", description = "OAuth 로그인을 하는 기능")
    @ApiResponse(
        responseCode = "200",
        description = "로그인에 성공했습니다."
    )
    @ApiResponse(
        responseCode = "400",
        description = "제공되지 않는 OAuth Service 입니다.",
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    @ApiResponse(
        responseCode = "502",
        description = """
            OAuth 로그인에 실패했습니다.
            OAuth 로그인 중 알 수 없는 에러가 발생했습니다.
        """,
        content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
    )
    public Api<AuthResponse> loginWithOAuth2Dev(OAuthRequest request, HttpServletResponse response);
}
