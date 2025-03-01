package co.kr.cocomu.study.repository;

import co.kr.cocomu.study.domain.StudyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyUserJpaRepository extends JpaRepository<StudyUser, Long> {

    boolean existsByUser_IdAndStudy_Id(Long userId, Long studyId);

}
