package co.kr.cocomu.study.repository;

import co.kr.cocomu.study.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageJpaRepository extends JpaRepository<Language, Long> {
}
