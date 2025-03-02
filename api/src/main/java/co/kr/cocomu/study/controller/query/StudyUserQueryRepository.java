package co.kr.cocomu.study.controller.query;

import co.kr.cocomu.study.dto.response.LeaderDto;
import java.util.List;
import java.util.Map;

public interface StudyUserQueryRepository {

    Map<Long, LeaderDto> findAllLeaderByStudies(List<Long> studyIds);
    LeaderDto findLeaderByStudyId(Long studyId);

}
