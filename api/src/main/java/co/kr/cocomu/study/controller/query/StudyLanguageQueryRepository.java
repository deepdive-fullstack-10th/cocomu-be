package co.kr.cocomu.study.controller.query;

import co.kr.cocomu.study.dto.response.LanguageDto;
import java.util.List;
import java.util.Map;

public interface StudyLanguageQueryRepository {

    Map<Long, List<LanguageDto>> findAllByLanguageByStudies(List<Long> studyIds);

    List<LanguageDto> findLanguageByStudyId(Long studyId);

}
