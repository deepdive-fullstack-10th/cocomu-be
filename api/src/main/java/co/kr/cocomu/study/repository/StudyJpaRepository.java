package co.kr.cocomu.study.repository;

import co.kr.cocomu.study.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyJpaRepository extends JpaRepository<Study, Long> {
}
