package co.kr.cocomu.user.repository;

import co.kr.cocomu.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
