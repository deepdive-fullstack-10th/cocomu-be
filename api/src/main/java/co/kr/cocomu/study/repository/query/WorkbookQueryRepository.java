package co.kr.cocomu.study.repository.query;

import co.kr.cocomu.study.dto.response.WorkbookDto;
import java.util.List;
import java.util.Map;

public interface WorkbookQueryRepository {

    Map<Long, List<WorkbookDto>> findWorkbookByStudies(List<Long> studyIds);
    List<WorkbookDto> findWorkbookByStudyId(Long studyId);

}
