package co.kr.cocomu.codingspace.domain;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import co.kr.cocomu.codingspace.dto.request.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.exception.CodingSpaceExceptionCode;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Study;
import co.kr.cocomu.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "cocomu_coding_space")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class CodingSpace {

    private static final int MIN_CODING_SPACE_USER_COUNT = 2;
    private static final int MAX_CODING_SPACE_USER_COUNT = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coding_space_id")
    private Long id;

    private String name;
    private String description;
    private String workbookUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    private int codingMinutes;
    private int currentUserCount;
    private int totalUserCount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private CodingSpaceStatus status;

    @Column(columnDefinition = "datetime(6)")
    private LocalDateTime startTime;

    @Column(columnDefinition = "datetime(6)")
    private LocalDateTime finishTime;

    @CreatedDate
    @Column(nullable = false, columnDefinition = "datetime(6)")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "codingSpace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCase> testCases = new ArrayList<>();

    @OneToMany(mappedBy = "codingSpace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CodingSpaceTab> tabs = new ArrayList<>();

    private CodingSpace(final CreateCodingSpaceDto dto, final Study study) {
        this.study = study;
        this.language = study.getLanguage(dto.languageId());
        this.name = dto.name();
        this.description = dto.description();
        this.workbookUrl = dto.workbookUrl();
        this.codingMinutes = dto.timerTime();
        this.currentUserCount = 0;
        this.totalUserCount = dto.totalUserCount();
        this.status = CodingSpaceStatus.WAITING;

        dto.testcases().stream()
            .map(TestCase::createDefaultCase)
            .forEach(this::addTestCase);
    }

    public static CodingSpace createCodingSpace(final CreateCodingSpaceDto dto, final Study study, final User host) {
        validateCreateCodingSpace(dto);
        final CodingSpace codingSpace = new CodingSpace(dto, study);
        codingSpace.increaseCurrentUserCount();

        final CodingSpaceTab tab = CodingSpaceTab.createHost(codingSpace, host);
        codingSpace.tabs.add(tab);

        return codingSpace;
    }

    private static void validateCreateCodingSpace(final CreateCodingSpaceDto dto) {
        validateMaxUserCount(dto.totalUserCount());
        validateMinUserCount(dto.totalUserCount());
    }

    private static void validateMaxUserCount(final int totalUserCount) {
        if (totalUserCount > MAX_CODING_SPACE_USER_COUNT) {
            throw new BadRequestException(CodingSpaceExceptionCode.MAX_USER_COUNT_IS_FOUR);
        }
    }

    private static void validateMinUserCount(final int totalUserCount) {
        if (totalUserCount < MIN_CODING_SPACE_USER_COUNT) {
            throw new BadRequestException(CodingSpaceExceptionCode.MIN_USER_COUNT_IS_TWO);
        }
    }

    public void addTestCase(TestCase testCase) {
        this.testCases.add(testCase);
        testCase.setCodingSpace(this);
    }

    public CodingSpaceTab joinUser(final User user) {
        validateJoin(user);
        this.increaseCurrentUserCount();

        final CodingSpaceTab tab = CodingSpaceTab.createMember(this, user);
        this.tabs.add(tab);

        return tab;
    }


    public void increaseCurrentUserCount() {
        currentUserCount++;
    }

    private void validateJoin(final User user) {
        validateWaitingStatus();
        validateParticipation(user);
        validateJoinable();
    }

    private void validateParticipation(final User user) {
        final boolean participation = tabs.stream()
            .anyMatch(tab -> tab.checkParticipation(user));

        if (participation) {
            throw new BadRequestException(CodingSpaceExceptionCode.ALREADY_PARTICIPATION_SPACE);
        }
    }

    private void validateWaitingStatus() {
        if (getStatus() != CodingSpaceStatus.WAITING) {
            throw new BadRequestException(CodingSpaceExceptionCode.NOT_WAITING_STUDY);
        }
    }

    private void validateJoinable() {
        if (getCurrentUserCount() >= getTotalUserCount()) {
            throw new BadRequestException(CodingSpaceExceptionCode.OVER_USER_COUNT);
        }
    }

}
