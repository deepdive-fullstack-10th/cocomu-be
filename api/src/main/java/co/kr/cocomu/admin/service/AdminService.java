package co.kr.cocomu.admin.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final JudgeJpaRepository judgeJpaRepository;
    private final LanguageJpaRepository languageJpaRepository;

    public JudgeResponse addJudge(final CreateJudgeRequest dto) {
        final Judge judge = Judge.of(dto.judgeName(), dto.judgeImageUrl());
        return judgeJpaRepository.save(judge).toDto();
    }

    public LanguageResponse addLanguage(final CreateLanguageRequest dto) {
        final Language language = Language.of(dto.languageName(), dto.languageImageUrl());
        return languageJpaRepository.save(language).toDto();
    }

    public void deleteLanguage(final Long languageId) {
        final Language language = languageJpaRepository.findById(languageId)
            .orElseThrow(() -> new NotFoundException(AdminExceptionCode.NOT_FOUND_LANGUAGE));
        languageJpaRepository.delete(language);
    }

    public void deleteJudge(final Long judgeId) {
        final Judge judge = judgeJpaRepository.findById(judgeId)
            .orElseThrow(() -> new NotFoundException(AdminExceptionCode.NOT_FOUND_JUDGE));
        judgeJpaRepository.delete(judge);
    }

}
