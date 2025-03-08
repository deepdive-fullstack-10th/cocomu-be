package co.kr.cocomu.codingspace.repository;

import co.kr.cocomu.codingspace.domain.CodingSpaceTab;
import co.kr.cocomu.codingspace.domain.vo.TabStatus;
import co.kr.cocomu.codingspace.repository.query.CodingSpaceTabQuery;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodingSpaceTabRepository extends JpaRepository<CodingSpaceTab, Long>, CodingSpaceTabQuery {

    boolean existsByUserIdAndCodingSpaceId(Long userId, Long codingSpaceId);

    Optional<CodingSpaceTab> findByUserIdAndCodingSpaceId(Long userId, Long codingSpaceId);

    boolean existsByIdAndStatus(Long id, TabStatus status);

}
