package co.kr.cocomu.study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.study.domain.vo.StudyRole;
import co.kr.cocomu.study.domain.vo.StudyStatus;
import co.kr.cocomu.study.domain.vo.StudyUserStatus;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.exception.StudyExceptionCode;
import co.kr.cocomu.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudyUserTest {

    private CreatePublicStudyDto dto;
    private Study mockStudy;
    private User mockUser;

    @BeforeEach
    void setUp() {
        dto = new CreatePublicStudyDto("코딩 스터디", List.of(), List.of(), "스터디", 10);
        mockStudy = Study.createPublicStudy(dto);
        mockUser = User.createUser("코코무");
    }

    @Test
    void 스터디에_리더로_참여_할_수_있다() {
        // given
        // when
        StudyUser studyUser = StudyUser.joinStudy(mockStudy, mockUser, StudyRole.LEADER);
        // then
        assertThat(studyUser.getStudyRole()).isEqualTo(StudyRole.LEADER);
        assertThat(studyUser.getStudyUserStatus()).isEqualTo(StudyUserStatus.JOIN);
    }

    @Test
    void 스터디에_일반_스터디원으로_참여_할_수_있다() {
        // given
        // when
        StudyUser studyUser = StudyUser.joinStudy(mockStudy, mockUser, StudyRole.NORMAL);
        // then
        assertThat(studyUser.getStudyRole()).isEqualTo(StudyRole.NORMAL);
        assertThat(studyUser.getStudyUserStatus()).isEqualTo(StudyUserStatus.JOIN);
    }

    @Test
    void 스터디에_참여하면_스터디_인원수가_증가한다() {
        // given
        int currentUserCount = mockStudy.getCurrentUserCount();

        // when
        StudyUser studyUser = StudyUser.joinStudy(mockStudy, mockUser, StudyRole.LEADER);

        // then
        assertThat(studyUser.getStudy().getCurrentUserCount()).isEqualTo(currentUserCount + 1);
    }

    @Test
    void 스터디에서_떠나면_스터디_인원수가_감소한다() {
        // given
        StudyUser studyUser = StudyUser.joinStudy(mockStudy, mockUser, StudyRole.NORMAL);
        Study study = studyUser.getStudy();
        int currentUserCount = study.getCurrentUserCount();

        // when
        studyUser.leaveStudy();

        // then
        assertThat(study.getCurrentUserCount()).isEqualTo(currentUserCount - 1);
        assertThat(studyUser.getStudyUserStatus()).isEqualTo(StudyUserStatus.LEAVE);
    }

    @Test
    void 스터디장은_스터디를_나갈_수_없다() {
        // given
        StudyUser studyUser = StudyUser.joinStudy(mockStudy, mockUser, StudyRole.LEADER);

        // when & then
        assertThatThrownBy(studyUser::leaveStudy)
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", StudyExceptionCode.LEADER_MUST_USE_REMOVE);
    }

    @Test
    void 스터디장은_스터디를_제거할_수_있다() {
        // given
        StudyUser studyUser = StudyUser.joinStudy(mockStudy, mockUser, StudyRole.LEADER);
        Study study = studyUser.getStudy();
        int currentUserCount = study.getCurrentUserCount();

        // when
        studyUser.removeStudy();

        // then
        assertThat(studyUser.getStudyUserStatus()).isEqualTo(StudyUserStatus.LEAVE);
        assertThat(study.getStatus()).isEqualTo(StudyStatus.REMOVE);
        assertThat(study.getCurrentUserCount()).isEqualTo(currentUserCount - 1);
    }

    @Test
    void 스터디원은_스터디를_제거할_수_없다() {
        // given
        StudyUser studyUser = StudyUser.joinStudy(mockStudy, mockUser, StudyRole.NORMAL);

        // when & then
        assertThatThrownBy(studyUser::removeStudy)
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", StudyExceptionCode.ONLY_LEADER_CAN_REMOVE_STUDY);
    }

}