package co.kr.cocomu.auth.controller.docs;

import co.kr.cocomu.auth.dto.response.AuthResponse;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.exception.dto.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "002. COCOMU USER AUTH", description = "코코무 사용자 인증 관련 API")
public interface AuthControllerDocs {

    @Operation(summary = "백도어 로그인", description = "개발용으로 임시 로그인을 하는 기능")
    @ApiResponse(
        responseCode = "1100",
        description = "로그인에 성공했습니다."
    )
    Api<AuthResponse> login(HttpServletResponse response);

}
