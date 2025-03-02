package co.kr.cocomu.study.domain;


import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.repository.TimeBaseEntity;
import co.kr.cocomu.study.domain.vo.StudyRole;
import co.kr.cocomu.study.domain.vo.StudyUserStatus;
import co.kr.cocomu.study.exception.StudyExceptionCode;
import co.kr.cocomu.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cocomu_study_user", uniqueConstraints = @UniqueConstraint(columnNames = {"study_id", "user_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyUser extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private StudyRole role;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private StudyUserStatus status;

    private StudyUser(final Study study, final User user, final StudyRole role) {
        this.study = study;
        this.user = user;
        this.role = role;
        this.status = StudyUserStatus.JOIN;
    }

    public static StudyUser joinStudy(final Study study, final User user, final StudyRole studyRole) {
        study.increaseCurrentUserCount();
        return new StudyUser(study, user, studyRole);
    }

    public void leaveStudy() {
        validateNormalRole();
        status = StudyUserStatus.LEAVE;
        study.decreaseCurrentUserCount();
    }

    public void removeStudy() {
        validateLeaderRole();
        study.decreaseCurrentUserCount();
        study.removeStudy();
        status = StudyUserStatus.LEAVE;
    }

    private void validateNormalRole() {
        if (role != StudyRole.NORMAL) {
            throw new BadRequestException(StudyExceptionCode.LEADER_MUST_USE_REMOVE);
        }
    }

    private void validateLeaderRole() {
        if (role != StudyRole.LEADER) {
            throw new BadRequestException(StudyExceptionCode.ONLY_LEADER_CAN_REMOVE_STUDY);
        }
    }

    public Long getStudyId() {
        return study.getId();
    }

}
