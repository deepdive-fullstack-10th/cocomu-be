package co.kr.cocomu.study.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import co.kr.cocomu.common.BaseControllerTest;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.template.GetRequestTemplate;
import co.kr.cocomu.common.template.PostRequestTemplate;
import co.kr.cocomu.study.controller.code.StudyApiCode;
import co.kr.cocomu.study.controller.query.StudyQueryRepository;
import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Workbook;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.dto.request.GetAllStudyFilterDto;
import co.kr.cocomu.study.dto.response.AllStudyPageDto;
import co.kr.cocomu.study.dto.response.LanguageDto;
import co.kr.cocomu.study.dto.response.StudyPageDto;
import co.kr.cocomu.study.dto.response.WorkbookDto;
import co.kr.cocomu.study.repository.LanguageJpaRepository;
import co.kr.cocomu.study.repository.WorkbookJpaRepository;
import co.kr.cocomu.study.service.StudyService;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@WebMvcTest(StudyController.class)
class StudyControllerTest extends BaseControllerTest {

    private static final String PATH_PREFIX = "/api/v1/studies";

    @MockBean private StudyService studyService;
    @MockBean private StudyQueryRepository studyQueryRepository;
    @MockBean private LanguageJpaRepository languageJpaRepository;
    @MockBean private WorkbookJpaRepository workbookJpaRepository;

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

    @Test
    void 공개방_참여_요청이_성공한다() {
        // given
        when(studyService.joinPublicStudy(1L, 1L)).thenReturn(1L);

        // when
        String path = PATH_PREFIX + "/public/1/join";
        ValidatableMockMvcResponse response = PostRequestTemplate.execute(path);

        // then
        Api<Long> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(StudyApiCode.JOIN_STUDY_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(StudyApiCode.JOIN_STUDY_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(1L);
    }

    @Test
    void 전체_스터디_조회_요청이_성공한다() {
        // given
        AllStudyPageDto mockResult = 전체스터디조회설정(1L);

        // when
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(PATH_PREFIX);

        // then
        전체스터디조회결과(response, mockResult);
    }

    @Test
    void 인증_정보_없이_전체_스터디_조회_요청이_성공한다() {
        // given
        AllStudyPageDto mockResult = 전체스터디조회설정(null);

        // when
        ValidatableMockMvcResponse response = GetRequestTemplate.executeNoAuth(PATH_PREFIX);

        // then
        전체스터디조회결과(response, mockResult);
    }

    @Test
    void 인증_토큰이_있을_때_전체_스터디_언어_조회가_된다() {
        // given
        List<Language> languages = 언어조회설정();

        // when
        String path = PATH_PREFIX + "/languages";
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(path);

        // then
        언어조회결과(languages, response);
    }

    @Test
    void 인증_토큰이_없을_때_전체_스터디_언어_조회가_된다() {
        // given
        List<Language> languages = 언어조회설정();

        // when
        String path = PATH_PREFIX + "/languages";
        ValidatableMockMvcResponse response = GetRequestTemplate.executeNoAuth(path);

        // then
        언어조회결과(languages, response);
    }

    @Test
    void 인증_토큰_추가시_전체_스터디_문제집_조회가_된다() {
        // given
        List<Workbook> workbooks = 문제집조회설정();

        // when
        String path = PATH_PREFIX + "/workbooks";
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(path);

        // then
        문제집조회결과(workbooks, response);
    }

    @Test
    void 인증_없이_전체_스터디_문제집_조회가_된다() {
        // given
        List<Workbook> workbooks = 문제집조회설정();

        // when
        String path = PATH_PREFIX + "/workbooks";
        ValidatableMockMvcResponse response = GetRequestTemplate.executeNoAuth(path);

        // then
        문제집조회결과(workbooks, response);
    }

    @Test
    void 스터디_정보페이지_조회가_된다() {
        // given
        StudyPageDto mockResult = new StudyPageDto();
        when(studyQueryRepository.findStudyPagesByStudyId(1L, null)).thenReturn(mockResult);

        // when
        String path = PATH_PREFIX + "/1/studyInfo";
        ValidatableMockMvcResponse response = GetRequestTemplate.executeNoAuth(path);

        // then
        Api<StudyPageDto> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(StudyApiCode.GET_STUDY_INFO_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(StudyApiCode.GET_STUDY_INFO_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(mockResult);
    }

    /*
    ===================   아래로 공통 테스트 설정입니다.  ===================
     */
    private @NotNull AllStudyPageDto 전체스터디조회설정(Long uesrId) {
        GetAllStudyFilterDto dto = new GetAllStudyFilterDto(null, null, null, null, null, null);
        AllStudyPageDto mockResult = new AllStudyPageDto(10L, List.of());
        when(studyQueryRepository.findTop20StudyPagesWithFilter(dto, uesrId)).thenReturn(mockResult);
        return mockResult;
    }

    private static void 전체스터디조회결과(final ValidatableMockMvcResponse response, final AllStudyPageDto mockResult) {
        Api<AllStudyPageDto> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(StudyApiCode.GET_ALL_STUDIES_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(StudyApiCode.GET_ALL_STUDIES_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(mockResult);
    }

    private @NotNull List<Language> 언어조회설정() {
        Language language1 = Language.of("JAVA", "이미지URL");
        Language language2 = Language.of("PYTHON", "이미지URL");
        List<Language> languages = List.of(language1, language2);
        when(languageJpaRepository.findAll()).thenReturn(languages);
        return languages;
    }

    private static void 언어조회결과(final List<Language> languages, final ValidatableMockMvcResponse response) {
        List<LanguageDto> mockResult = languages.stream().map(Language::toDto).toList();
        Api<List<LanguageDto>> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(StudyApiCode.GET_ALL_LANGUAGE_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(StudyApiCode.GET_ALL_LANGUAGE_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(mockResult);
    }

    private @NotNull List<Workbook> 문제집조회설정() {
        Workbook workbook1 = Workbook.of("백준", "이미지URL");
        Workbook workbook2 = Workbook.of("프로그래머스", "이미지URL");
        List<Workbook> workbooks = List.of(workbook1, workbook2);
        when(workbookJpaRepository.findAll()).thenReturn(workbooks);
        return workbooks;
    }

    private static void 문제집조회결과(List<Workbook> workbooks, ValidatableMockMvcResponse response) {
        List<WorkbookDto> mockResult = workbooks.stream().map(Workbook::toDto).toList();
        Api<List<WorkbookDto>> result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(StudyApiCode.GET_ALL_WORKBOOK_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(StudyApiCode.GET_ALL_WORKBOOK_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(mockResult);
    }

}