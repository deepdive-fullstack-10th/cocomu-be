package co.kr.cocomu.study.repository.jpa;

import co.kr.cocomu.study.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
