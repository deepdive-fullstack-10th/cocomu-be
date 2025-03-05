package co.kr.cocomu.codingspace.domain;


import co.kr.cocomu.codingspace.domain.vo.CodingSpaceRole;
import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import co.kr.cocomu.codingspace.domain.vo.TabStatus;
import co.kr.cocomu.codingspace.exception.CodingSpaceExceptionCode;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.repository.TimeBaseEntity;
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
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cocomu_coding_space_tab")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CodingSpaceTab extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coding_space_tab_id")
    private Long id;

    @Column
    private String documentKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coding_space_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CodingSpace codingSpace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private CodingSpaceRole role;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private TabStatus status;

    private CodingSpaceTab(CodingSpace codingSpace, User user, CodingSpaceRole role) {
        this.documentKey = UUID.randomUUID().toString();
        this.codingSpace = codingSpace;
        this.user = user;
        this.status = TabStatus.JOIN;
        this.role = role;
    }

    public static CodingSpaceTab createMember(final CodingSpace codingSpace, final User user) {
        return new CodingSpaceTab(codingSpace, user, CodingSpaceRole.MEMBER);
    }

    public static CodingSpaceTab createHost(final CodingSpace codingSpace, final User user) {
        return new CodingSpaceTab(codingSpace, user, CodingSpaceRole.HOST);
    }

    public boolean checkParticipation(final User user) {
        return this.user.equals(user);
    }

    public void enterTab() {
        if (codingSpace.getStatus() == CodingSpaceStatus.FINISHED) {
            throw new BadRequestException(CodingSpaceExceptionCode.FINISHED_CODING_SPACE);
        }
        this.status = TabStatus.ACTIVE;
    }

    public void leaveTab() {
        if (codingSpace.getStatus() == CodingSpaceStatus.FINISHED) {
            throw new BadRequestException(CodingSpaceExceptionCode.FINISHED_CODING_SPACE);
        }
        this.status = TabStatus.INACTIVE;
    }

}
