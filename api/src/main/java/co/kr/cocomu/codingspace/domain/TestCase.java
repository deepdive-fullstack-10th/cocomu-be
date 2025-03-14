package co.kr.cocomu.codingspace.domain;

import co.kr.cocomu.codingspace.domain.vo.TestCaseType;
import co.kr.cocomu.codingspace.dto.request.CreateTestCaseDto;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cocomu_test_case")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_case_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coding_space_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @Setter
    private CodingSpace codingSpace;

    @Column(columnDefinition = "text")
    private String input;

    @Column(columnDefinition = "text")
    private String output;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private TestCaseType type;

    private TestCase(final CreateTestCaseDto dto) {
        this.input = dto.input();
        this.output = dto.output();
    }

    public static TestCase createDefaultCase(final CreateTestCaseDto dto) {
        final TestCase testCase = new TestCase(dto);
        testCase.type = TestCaseType.DEFAULT;
        return testCase;
    }

    public static TestCase createCustomCase(final CreateTestCaseDto dto) {
        final TestCase testCase = new TestCase(dto);
        testCase.type = TestCaseType.CUSTOM;
        return testCase;
    }

    public boolean checkAnswer(final String output) {
        return this.output.equals(output);
    }

}
