package co.kr.cocomu.codingspace.repository;

import co.kr.cocomu.codingspace.domain.CodingSpaceTab;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodingSpaceTabRepository extends JpaRepository<CodingSpaceTab, Long> {

    boolean existsByUserIdAndCodingSpaceId(Long userId, Long codingSpaceId);

}
