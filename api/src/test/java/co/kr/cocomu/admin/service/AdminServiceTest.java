package co.kr.cocomu.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import co.kr.cocomu.admin.dto.request.CreateJudgeRequest;
import co.kr.cocomu.admin.dto.request.CreateLanguageRequest;
import co.kr.cocomu.admin.dto.response.JudgeResponse;
import co.kr.cocomu.admin.dto.response.LanguageResponse;
import co.kr.cocomu.admin.exception.AdminExceptionCode;
import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.study.domain.Judge;
import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.repository.JudgeJpaRepository;
import co.kr.cocomu.study.repository.LanguageJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock private LanguageJpaRepository languageJpaRepository;
    @Mock private JudgeJpaRepository judgeJpaRepository;

    @InjectMocks private AdminService adminService;

    @Test
    void 문제집이_추가가_된다() {
        // given
        CreateJudgeRequest dto = new CreateJudgeRequest("백준", "이미지URL");
        Judge savedJudge = Judge.of("백준", "이미지URL");
        ReflectionTestUtils.setField(savedJudge, "id", 1L);

        given(judgeJpaRepository.save(any(Judge.class))).willReturn(savedJudge);

        // when
        JudgeResponse result = adminService.addJudge(dto);

        // then
        assertThat(result.judgeId()).isEqualTo(1L);
    }

    @Test
    void 언어가_추가_된다() {
        // given
        CreateLanguageRequest dto = new CreateLanguageRequest("자바", "이미지URL");
        Language savedLanguage = Language.of("자바", "이미지URL");
        ReflectionTestUtils.setField(savedLanguage, "id", 1L);

        given(languageJpaRepository.save(any(Language.class))).willReturn(savedLanguage);

        // when
        LanguageResponse result = adminService.addLanguage(dto);

        // then
        assertThat(result.languageId()).isEqualTo(1L);
    }

    @Test
    void 문제집이_삭제된다() {
        // given
        Judge judge = Judge.of("백준", "이미지 URL");
        given(judgeJpaRepository.findById(anyLong())).willReturn(Optional.of(judge));

        // when
        adminService.deleteJudge(1L);

        // then
        verify(judgeJpaRepository).delete(judge);
    }

    @Test
    void 존재하지_않는_문제집_삭제시_예외가_발생한다() {
        // given
        given(judgeJpaRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> adminService.deleteJudge(1L))
            .isInstanceOf(NotFoundException.class)
            .hasFieldOrPropertyWithValue("exceptionType", AdminExceptionCode.NOT_FOUND_JUDGE);
    }

    @Test
    void 언어가_삭제된다() {
        // given
        Language language = Language.of("자바", "이미지URL");
        given(languageJpaRepository.findById(anyLong())).willReturn(Optional.of(language));

        // when
        adminService.deleteLanguage(1L);

        // then
        verify(languageJpaRepository).delete(language);
    }

    @Test
    void 존재하지_않는_언어_삭제시_예외가_발생한다() {
        // given
        given(languageJpaRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> adminService.deleteLanguage(1L))
            .isInstanceOf(NotFoundException.class)
            .hasFieldOrPropertyWithValue("exceptionType", AdminExceptionCode.NOT_FOUND_LANGUAGE);
    }

}