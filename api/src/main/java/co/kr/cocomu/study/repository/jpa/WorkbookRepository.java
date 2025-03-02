package co.kr.cocomu.study.repository.jpa;

import co.kr.cocomu.study.domain.Workbook;
import co.kr.cocomu.study.repository.query.WorkbookQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkbookRepository extends JpaRepository<Workbook, Long>, WorkbookQueryRepository {
}
