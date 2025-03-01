package co.kr.cocomu.study.repository;

import co.kr.cocomu.study.domain.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkbookJpaRepository extends JpaRepository<Workbook, Long> {
}
