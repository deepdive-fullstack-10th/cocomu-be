package co.kr.cocomu.study.controller.query;

import co.kr.cocomu.study.dto.request.GetAllStudyFilterDto;
import co.kr.cocomu.study.dto.response.AllStudyPageDto;
import co.kr.cocomu.study.dto.response.StudyPageDto;

public interface StudyQueryRepository {

    AllStudyPageDto findTop20StudyPagesWithFilter(GetAllStudyFilterDto filter, Long userId);

    StudyPageDto findStudyPagesByStudyId(Long studyId, Long userId);

}
