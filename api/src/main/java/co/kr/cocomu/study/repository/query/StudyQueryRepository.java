package co.kr.cocomu.study.repository.query;

import co.kr.cocomu.study.dto.request.GetAllStudyFilterDto;
import co.kr.cocomu.study.dto.response.StudyCardDto;
import java.util.List;
import java.util.Optional;

public interface StudyQueryRepository {

    List<StudyCardDto> findTop12StudyCardsWithFilter(GetAllStudyFilterDto filter, Long userId);
    List<StudyCardDto> findTop20UserStudyCards(Long userId, Long viewerId, Long lastIndex);
    Long countStudyCardsWithFilter(GetAllStudyFilterDto filter, Long userId);
    Optional<StudyCardDto> findStudyPagesByStudyId(Long studyId, Long userId);

}
