package co.kr.cocomu.study.controller.query;

import co.kr.cocomu.study.dto.response.WorkbookDto;
import java.util.List;
import java.util.Map;

public interface StudyWorkbookQueryRepository {

    Map<Long, List<WorkbookDto>> findAllByWorkbookByStudies(List<Long> studyIds);
    List<WorkbookDto> findWorkbookByStudyId(Long studyId);

}
