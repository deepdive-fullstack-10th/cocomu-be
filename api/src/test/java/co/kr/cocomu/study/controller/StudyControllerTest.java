package co.kr.cocomu.study.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import co.kr.cocomu.common.BaseControllerTest;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.template.PostRequestTemplate;
import co.kr.cocomu.study.controller.code.StudyApiCode;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.service.StudyService;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@WebMvcTest(StudyController.class)
class StudyControllerTest extends BaseControllerTest {

    private static final String PATH_PREFIX = "/api/v1/studies";

    @MockBean private StudyService studyService;

    @Test
    void 공개방_생성_요청이_성공한다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코코무", List.of(), List.of(), "", 10);
        when(studyService.createPublicStudy(1L, dto)).thenReturn(1L);

        // when
        String path = PATH_PREFIX + "/public";
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(path, dto);

        // then
        Api<Long> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(StudyApiCode.CREATE_STUDY_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(StudyApiCode.CREATE_STUDY_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(1L);
    }

}