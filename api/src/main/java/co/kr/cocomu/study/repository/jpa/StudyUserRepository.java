package co.kr.cocomu.study.repository.jpa;

import co.kr.cocomu.study.domain.StudyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyUserRepository extends JpaRepository<StudyUser, Long> {

    boolean existsByUser_IdAndStudy_Id(Long userId, Long studyId);

}
