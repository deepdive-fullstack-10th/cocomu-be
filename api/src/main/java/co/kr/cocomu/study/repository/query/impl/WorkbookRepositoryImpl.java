package co.kr.cocomu.study.repository.query.impl;

import static co.kr.cocomu.study.domain.QStudyWorkbook.studyWorkbook;
import static co.kr.cocomu.study.domain.QWorkbook.workbook;

import co.kr.cocomu.study.dto.response.WorkbookDto;
import co.kr.cocomu.study.repository.query.WorkbookQueryRepository;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkbookRepositoryImpl implements WorkbookQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Map<Long, List<WorkbookDto>> findWorkbookByStudies(final List<Long> studyIds) {
        return queryFactory.select(studyWorkbook.study.id, workbook)
            .from(studyWorkbook)
            .join(workbook).on(studyWorkbook.workbook.id.eq(workbook.id))
            .where(studyWorkbook.study.id.in(studyIds))
            .transform(GroupBy.groupBy(studyWorkbook.study.id)
                .as(GroupBy.list(
                    Projections.fields(
                        WorkbookDto.class,
                        workbook.id.as("id"),
                        workbook.name.as("name"),
                        workbook.imageUrl.as("imageUrl")
                    )
                ))
            );
    }

    public List<WorkbookDto> findWorkbookByStudyId(final Long studyId) {
        return queryFactory.select(
                Projections.fields(
                    WorkbookDto.class,
                    workbook.id.as("id"),
                    workbook.name.as("name"),
                    workbook.imageUrl.as("imageUrl")
                ))
            .from(studyWorkbook)
            .join(workbook).on(studyWorkbook.workbook.id.eq(workbook.id))
            .where(studyWorkbook.study.id.eq(studyId))
            .fetch();
    }

}
