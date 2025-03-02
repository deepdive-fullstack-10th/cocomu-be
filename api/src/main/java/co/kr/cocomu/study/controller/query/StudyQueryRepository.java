package co.kr.cocomu.study.controller.query;

import co.kr.cocomu.study.dto.request.GetAllStudyFilterDto;
import co.kr.cocomu.study.dto.response.AllStudyPageDto;

public interface StudyQueryRepository {

    AllStudyPageDto findTop20StudyPagesWithFilter(GetAllStudyFilterDto filter, Long userId);

}
