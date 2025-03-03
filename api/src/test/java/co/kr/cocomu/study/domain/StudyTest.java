package co.kr.cocomu.study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.study.domain.vo.StudyStatus;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.exception.StudyExceptionCode;
import co.kr.cocomu.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudyTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.createUser("코코무");
    }

    @Test
    void 생성한_스터디의_현재_참여인원수는_0명이다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        // when
        Study publicStudy = Study.createPublicStudy(dto);
        // then
        assertThat(publicStudy.getCurrentUserCount()).isEqualTo(0);
    }

    @Test
    void 스터디_인원이_증가된다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study publicStudy = Study.createPublicStudy(dto);

        // when
        publicStudy.increaseCurrentUserCount();

        // then
        assertThat(publicStudy.getCurrentUserCount()).isEqualTo(1);
    }

    @Test
    void 스터디_인원이_감소된다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study publicStudy = Study.createPublicStudy(dto);
        publicStudy.increaseCurrentUserCount();

        // when
        publicStudy.decreaseCurrentUserCount();

        // then
        assertThat(publicStudy.getCurrentUserCount()).isEqualTo(0);
    }

    @Test
    void 스터디_인원이_없다면_감소되지_않는다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study publicStudy = Study.createPublicStudy(dto);

        // when
        publicStudy.decreaseCurrentUserCount();

        // then
        assertThat(publicStudy.getCurrentUserCount()).isEqualTo(0);
    }

    @Test
    void 스터디에서_활용할_문제집을_추가한다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study publicStudy = Study.createPublicStudy(dto);
        Workbook workBook = Workbook.of("백준", "image");

        // when
        publicStudy.addBook(workBook);

        // then
        assertThat(publicStudy.getWorkbooks()).hasSize(1);
    }

    @Test
    void 스터디에서_활용할_문제집_리스트를_추가한다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study publicStudy = Study.createPublicStudy(dto);
        List<Workbook> workbooks = List.of(Workbook.of("백준", "image"), Workbook.of("프로그래머스", "image"));

        // when
        publicStudy.addBooks(workbooks);

        // then
        assertThat(publicStudy.getWorkbooks()).hasSize(2);
    }

    @Test
    void 스터디에서_활용할_언어를_추가한다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study publicStudy = Study.createPublicStudy(dto);
        Language language = Language.of("언어", "image");

        // when
        publicStudy.addLanguage(language);

        // then
        assertThat(publicStudy.getLanguages()).hasSize(1);
    }

    @Test
    void 스터디에서_활용할_언어_리스트를_추가한다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study publicStudy = Study.createPublicStudy(dto);
        List<Language> languages = List.of(Language.of("파이썬", "image"), Language.of("자바", "image"));

        // when
        publicStudy.addLanguages(languages);

        // then
        assertThat(publicStudy.getLanguages()).hasSize(2);
    }

    @Test
    void 스터디가_제거된다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study study = Study.createPublicStudy(dto);

        // when
        study.removeStudy();

        // then
        assertThat(study.getCurrentUserCount()).isEqualTo(0);
        assertThat(study.getStatus()).isEqualTo(StudyStatus.REMOVE);
    }

    @Test
    void 스터디원이_남아있다면_제거되지_않는다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study study = Study.createPublicStudy(dto);
        study.increaseCurrentUserCount();
        study.increaseCurrentUserCount();

        // when & then
        assertThatThrownBy(study::removeStudy)
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", StudyExceptionCode.REMAINING_USER);
    }

    @Test
    void 스터디에서_사용하는_언어_정보를_가져올_수_있다() {
        // given
        Study study = new Study();
        Language mockLanguage = mock(Language.class);
        when(mockLanguage.getId()).thenReturn(1L);
        study.addLanguage(mockLanguage);

        // when
        Language result = study.getLanguage(1L);

        // then
        assertThat(result).isEqualTo(mockLanguage);
    }

    @Test
    void 스터디에서_사용하는_언어_정보가_없다면_예외가_발생한다() {
        // given
        Study study = new Study();

        // when & then
        assertThatThrownBy(() -> study.getLanguage(1L))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", StudyExceptionCode.INVALID_STUDY_LANGUAGE);
    }

}