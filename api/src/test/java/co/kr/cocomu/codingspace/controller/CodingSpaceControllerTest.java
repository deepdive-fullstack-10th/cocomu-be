package co.kr.cocomu.codingspace.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.controller.code.CodingSpaceApiCode;
import co.kr.cocomu.codingspace.dto.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.service.CodingSpaceCommandService;
import co.kr.cocomu.common.BaseControllerTest;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.template.PostRequestTemplate;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@WebMvcTest(CodingSpaceController.class)
class CodingSpaceControllerTest extends BaseControllerTest {

    private static final String PATH_PREFIX = "/api/v1/coding-spaces";

    @MockBean private CodingSpaceCommandService codingSpaceCommandService;

    @Test
    void 코딩_스페이스_생성_요청이_성공한다() {
        // given
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 10, 30, 1L, "", "", "");
        when(codingSpaceCommandService.createCodingSpace(dto, 1L)).thenReturn(1L);

        // when
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(PATH_PREFIX, dto);

        // then
        Api<Long> result =  response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(CodingSpaceApiCode.CREATE_CODING_SPACE_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(CodingSpaceApiCode.CREATE_CODING_SPACE_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(1L);
    }

}