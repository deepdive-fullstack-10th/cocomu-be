package co.kr.cocomu.study.repository.jpa;

import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.repository.query.LanguageQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long>, LanguageQueryRepository {
}
