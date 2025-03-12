package co.kr.cocomu.codingspace.repository;

import co.kr.cocomu.codingspace.domain.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
}
