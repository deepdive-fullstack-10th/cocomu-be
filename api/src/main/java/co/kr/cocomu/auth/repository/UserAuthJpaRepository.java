package co.kr.cocomu.auth.repository;

import co.kr.cocomu.auth.domain.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthJpaRepository extends JpaRepository<UserAuth, String> {
}
