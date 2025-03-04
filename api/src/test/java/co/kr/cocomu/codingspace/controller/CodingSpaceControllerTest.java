package co.kr.cocomu.codingspace.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.controller.code.CodingSpaceApiCode;
import co.kr.cocomu.codingspace.dto.request.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.dto.request.CreateTestCaseDto;
import co.kr.cocomu.codingspace.dto.request.FilterDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceIdDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpaceTabIdDto;
import co.kr.cocomu.codingspace.dto.response.CodingSpacesDto;
import co.kr.cocomu.codingspace.dto.response.WritePageDto;
import co.kr.cocomu.codingspace.service.CodingSpaceCommandService;
import co.kr.cocomu.codingspace.service.CodingSpaceQueryService;
import co.kr.cocomu.common.BaseControllerTest;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.template.GetRequestTemplate;
import co.kr.cocomu.common.template.PostRequestTemplate;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@WebMvcTest(CodingSpaceController.class)
class CodingSpaceControllerTest extends BaseControllerTest {

    private static final String PATH_PREFIX = "/api/v1/coding-spaces";

    @MockBean private CodingSpaceCommandService codingSpaceCommandService;
    @MockBean private CodingSpaceQueryService codingSpaceQueryService;

    @Test
    void 코딩_스페이스_생성_요청이_성공한다() {
        // given
        CreateTestCaseDto testCaseDto = new CreateTestCaseDto("input", "output");
        CreateCodingSpaceDto dto = new CreateCodingSpaceDto(1L, 10, 30, 1L, "", "", "", List.of(testCaseDto));
        when(codingSpaceCommandService.createCodingSpace(dto, 1L)).thenReturn(1L);

        // when
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(PATH_PREFIX, dto);

        // then
        Api<CodingSpaceIdDto> result =  response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(CodingSpaceApiCode.CREATE_CODING_SPACE_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(CodingSpaceApiCode.CREATE_CODING_SPACE_SUCCESS.getMessage());
        assertThat(result.result().codingSpaceId()).isEqualTo(1L);
    }

    @Test
    void 코딩_스페이스_참여_요청이_성공한다() {
        // given
        String mockResult = "Tab_ID";
        when(codingSpaceCommandService.joinCodingSpace(1L, 1L)).thenReturn(mockResult);

        // when
        String path = PATH_PREFIX + "/1";
        ValidatableMockMvcResponse response = PostRequestTemplate.execute(path);

        // then
        Api<CodingSpaceTabIdDto> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(CodingSpaceApiCode.JOIN_CODING_SPACE_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(CodingSpaceApiCode.JOIN_CODING_SPACE_SUCCESS.getMessage());
        assertThat(result.result().codingSpaceTabId()).isEqualTo(mockResult);
    }

    @Test
    void 코딩_스페이스_생성_페이지_조회에_성공한다() {
        // given
        when(codingSpaceQueryService.getStudyLanguages(1L, 1L)).thenReturn(List.of());

        // when
        String path = PATH_PREFIX + "/write?studyId=1";
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(path);

        // then
        Api<WritePageDto> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(CodingSpaceApiCode.GET_WRITE_PAGE_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(CodingSpaceApiCode.GET_WRITE_PAGE_SUCCESS.getMessage());
        assertThat(result.result().languages()).isEqualTo(List.of());
    }

    @Test
    void 코딩_스페이스_목록_조회하기() {
        // given
        CodingSpacesDto mockResult = CodingSpacesDto.of(List.of(), Map.of());
        when(codingSpaceQueryService.getCodingSpaces(eq(1L), eq(1L), any(FilterDto.class))).thenReturn(mockResult);

        // when
        String path = PATH_PREFIX + "/studies/1";
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(path);

        // then
        Api<CodingSpacesDto> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(CodingSpaceApiCode.GET_CODING_SPACES_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(CodingSpaceApiCode.GET_CODING_SPACES_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(mockResult);
    }

}