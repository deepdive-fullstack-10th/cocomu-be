package co.kr.cocomu.study.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
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
    void 생성한_스터디의_현재_참여인원수는_1명이다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        // when
        Study publicStudy = Study.createPublicStudy(user, dto);
        // then
        assertThat(publicStudy.getCurrentUserCount()).isEqualTo(1);
    }

    @Test
    void 스터디_인원이_증가된다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study publicStudy = Study.createPublicStudy(user, dto);

        // when
        publicStudy.increaseCurrentUserCount();

        // then
        assertThat(publicStudy.getCurrentUserCount()).isEqualTo(2);
    }

    @Test
    void 스터디에서_활용할_문제집을_추가한다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study publicStudy = Study.createPublicStudy(user, dto);
        Judge judge = Judge.of("백준", "image");

        // when
        publicStudy.addJudge(judge);

        // then
        assertThat(publicStudy.getJudges()).hasSize(1);
    }

    @Test
    void 스터디에서_활용할_문제집_리스트를_추가한다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study publicStudy = Study.createPublicStudy(user, dto);
        List<Judge> judges = List.of(Judge.of("백준", "image"), Judge.of("프로그래머스", "image"));

        // when
        publicStudy.addJudges(judges);

        // then
        assertThat(publicStudy.getJudges()).hasSize(2);
    }

    @Test
    void 스터디에서_활용할_언어를_추가한다() {
        // given
        CreatePublicStudyDto dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        Study publicStudy = Study.createPublicStudy(user, dto);
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
        Study publicStudy = Study.createPublicStudy(user, dto);
        List<Language> languages = List.of(Language.of("파이썬", "image"), Language.of("자바", "image"));

        // when
        publicStudy.addLanguages(languages);

        // then
        assertThat(publicStudy.getLanguages()).hasSize(2);
    }

}