package co.kr.cocomu.codingspace.domain;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import co.kr.cocomu.codingspace.dto.CreateCodingSpaceDto;
import co.kr.cocomu.codingspace.exception.CodingSpaceExceptionCode;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.study.domain.Language;
import co.kr.cocomu.study.domain.Study;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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

    private CodingSpace(final CreateCodingSpaceDto dto) {
        this.name = dto.name();
        this.description = dto.description();
        this.workbookUrl = dto.workbookUrl();
        this.codingMinutes = dto.timerTime();
        this.currentUserCount = 0;
        this.totalUserCount = dto.totalUserCount();
        this.status = CodingSpaceStatus.WAITING;
    }

    public static CodingSpace createCodingSpace(final CreateCodingSpaceDto dto, final Study study) {
        validateCreateCodingSpace(dto);
        final CodingSpace codingSpace = new CodingSpace(dto);
        codingSpace.study = study;
        codingSpace.language = study.getLanguage(dto.languageId());
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

}
