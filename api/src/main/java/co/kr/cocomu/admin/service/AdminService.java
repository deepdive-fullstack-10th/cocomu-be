package co.kr.cocomu.admin.service;

import co.kr.cocomu.admin.dto.request.CreateWorkbookRequest;
import co.kr.cocomu.admin.dto.request.CreateLanguageRequest;
import co.kr.cocomu.admin.dto.response.WorkbookResponse;
import co.kr.cocomu.admin.dto.response.LanguageResponse;
import co.kr.cocomu.admin.exception.AdminExceptionCode;
import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.study.domain.Workbook;
import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.repository.WorkbookJpaRepository;
import co.kr.cocomu.study.repository.LanguageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final WorkbookJpaRepository workbookJpaRepository;
    private final LanguageJpaRepository languageJpaRepository;

    public WorkbookResponse addWorkbook(final CreateWorkbookRequest dto) {
        final Workbook workBook = Workbook.of(dto.workbookName(), dto.workbookImageUrl());
        return workbookJpaRepository.save(workBook).toDto();
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

    public void deleteWorkbook(final Long workbookId) {
        final Workbook workBook = workbookJpaRepository.findById(workbookId)
            .orElseThrow(() -> new NotFoundException(AdminExceptionCode.NOT_FOUND_WORKBOOK));
        workbookJpaRepository.delete(workBook);
    }

}
