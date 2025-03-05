package co.kr.cocomu.codingspace.repository;

import co.kr.cocomu.codingspace.domain.CodingSpace;
import co.kr.cocomu.codingspace.repository.query.CodingSpaceQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodingSpaceRepository extends JpaRepository<CodingSpace, Long>, CodingSpaceQuery {
}
