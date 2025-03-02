package co.kr.cocomu.study.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.study.dto.request.CreatePublicStudyDto;
import co.kr.cocomu.study.exception.StudyExceptionCode;
import co.kr.cocomu.study.repository.jpa.StudyRepository;
import co.kr.cocomu.study.repository.jpa.StudyUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyDomainServiceTest {

    @Mock private StudyRepository studyRepository;
    @Mock private StudyUserRepository studyUserRepository;

    @InjectMocks private StudyDomainService studyDomainService;

    @Test
    void 스터디_ID로_스터디를_찾을_수_있다() {
        // given
        final CreatePublicStudyDto dto = new CreatePublicStudyDto("스터디", null, null, null, 0);
        final Study mockStudy = Study.createPublicStudy(dto);

        when(studyRepository.findById(anyLong())).thenReturn(Optional.of(mockStudy));

        // when
        Study actualStudy = studyDomainService.getStudyWithThrow(1L);

        // then
        assertThat(actualStudy).isEqualTo(mockStudy);
    }

    @Test
    void 존재하지_않는_스터디_조회_시_예외가_발생한다() {
        // given
        when(studyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> studyDomainService.getStudyWithThrow(1L))
            .isInstanceOf(NotFoundException.class)
            .hasFieldOrPropertyWithValue("exceptionType", StudyExceptionCode.NOT_FOUND_STUDY);
    }

    @Test
    void 참여중인_스터디라면_예외가_발생한다() {
        // given
        Long userId = 1L;
        Long studyId = 1L;

        when(studyUserRepository.isUserJoinedStudy(userId, studyId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> studyDomainService.validateStudyParticipation(userId, studyId))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", StudyExceptionCode.ALREADY_PARTICIPATION_STUDY);
    }

    @Test
    void 참여중이지_않은_스터디라면_정상_통과_한다() {
        // given
        Long userId = 1L;
        Long studyId = 1L;

        when(studyUserRepository.isUserJoinedStudy(userId, studyId)).thenReturn(false);

        // when & then
        assertThatCode(() -> studyDomainService.validateStudyParticipation(userId, studyId))
            .doesNotThrowAnyException();
    }

    @Test
    void 스터디에_참여중이지_않으면_예외가_발생한다() {
        // given
        Long userId = 1L;
        Long studyId = 1L;

        when(studyUserRepository.isUserJoinedStudy(userId, studyId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> studyDomainService.validateStudyMembership(userId, studyId))
            .isInstanceOf(BadRequestException.class)
            .hasFieldOrPropertyWithValue("exceptionType", StudyExceptionCode.NO_PARTICIPATION_USER);
    }

    @Test
    void 참여중인_스터디라면_정상_통과한다() {
        // given
        Long userId = 1L;
        Long studyId = 1L;

        when(studyUserRepository.isUserJoinedStudy(userId, studyId)).thenReturn(true);

        // when & then
        assertThatCode(() ->studyDomainService.validateStudyMembership(userId, studyId))
            .doesNotThrowAnyException();
    }

}