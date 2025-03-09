package co.kr.cocomu.study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    void 스터디에_리더로_참여_할_수_있다() {
        // given
        Study mockStudy = mock(Study.class);
        User mockUser = mock(User.class);

        // when
        StudyUser studyUser = StudyUser.createLeader(mockStudy, mockUser);

        // then
        assertThat(studyUser.getRole()).isEqualTo(StudyRole.LEADER);
        assertThat(studyUser.getStatus()).isEqualTo(StudyUserStatus.JOIN);
    }

    @Test
    void 스터디는_일반_스터디원으로_참여_할_수_있다() {
        // given
        Study mockStudy = mock(Study.class);
        User mockUser = mock(User.class);

        // when
        StudyUser studyUser = StudyUser.createMember(mockStudy, mockUser);
        // then
        assertThat(studyUser.getRole()).isEqualTo(StudyRole.MEMBER);
        assertThat(studyUser.getStatus()).isEqualTo(StudyUserStatus.JOIN);
    }

    @Test
    void 일반_회원은_스터디에서_나갈_수_있다() {
        // given
        Study mockStudy = mock(Study.class);
        User mockUser = mock(User.class);
        StudyUser studyUser = StudyUser.createMember(mockStudy, mockUser);
        doNothing().when(mockStudy).leaveUser();

        // when
        studyUser.leaveStudy();

        // then
        assertThat(studyUser.getStatus()).isEqualTo(StudyUserStatus.LEAVE);
    }

    @Test
    void 스터디장은_스터디를_나갈_수_없다() {
        // given
        Study mockStudy = mock(Study.class);
        User mockUser = mock(User.class);
        StudyUser studyUser = StudyUser.createLeader(mockStudy, mockUser);

        // when & then
        assertThatThrownBy(studyUser::leaveStudy)
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", StudyExceptionCode.LEADER_MUST_USE_REMOVE);
    }

    @Test
    void 스터디장은_스터디를_제거할_수_있다() {
        // given
        Study mockStudy = mock(Study.class);
        User mockUser = mock(User.class);
        StudyUser studyUser = StudyUser.createLeader(mockStudy, mockUser);
        doNothing().when(mockStudy).leaveUser();

        // when
        studyUser.removeStudy();

        // then
        assertThat(studyUser.getStatus()).isEqualTo(StudyUserStatus.LEAVE);
    }

    @Test
    void 일반_스터디원은_스터디를_제거할_수_있다() {
        // given
        Study mockStudy = mock(Study.class);
        User mockUser = mock(User.class);
        StudyUser studyUser = StudyUser.createMember(mockStudy, mockUser);

        // when & then
        assertThatThrownBy(studyUser::removeStudy)
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", StudyExceptionCode.ONLY_LEADER_CAN_REMOVE_STUDY);
    }

}