package co.kr.cocomu.codingspace.repository;

import co.kr.cocomu.codingspace.domain.CodingSpace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodingSpaceRepository extends JpaRepository<CodingSpace, Long> {
}
