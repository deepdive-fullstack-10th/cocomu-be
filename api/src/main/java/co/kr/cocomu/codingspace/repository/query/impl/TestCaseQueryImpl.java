package co.kr.cocomu.codingspace.repository.query.impl;

import static co.kr.cocomu.codingspace.domain.QTestCase.testCase;

import co.kr.cocomu.codingspace.dto.response.TestCaseDto;
import co.kr.cocomu.codingspace.repository.query.TestCaseQuery;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TestCaseQueryImpl implements TestCaseQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TestCaseDto> findTestCases(final Long codingSpaceId) {
        return queryFactory
            .select(Projections.constructor(TestCaseDto.class,
                testCase.id,
                testCase.input,
                testCase.output,
                testCase.type
            ))
            .from(testCase)
            .where(testCase.codingSpace.id.eq(codingSpaceId))
            .fetch();
    }

}
