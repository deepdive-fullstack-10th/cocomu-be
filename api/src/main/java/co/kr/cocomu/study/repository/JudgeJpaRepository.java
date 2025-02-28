package co.kr.cocomu.study.repository;

import co.kr.cocomu.study.domain.Judge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JudgeJpaRepository extends JpaRepository<Judge, Long> {
}
