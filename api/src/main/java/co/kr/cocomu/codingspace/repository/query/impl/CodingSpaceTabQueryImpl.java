package co.kr.cocomu.codingspace.repository.query.impl;

import static co.kr.cocomu.codingspace.domain.QCodingSpaceTab.codingSpaceTab;
import static co.kr.cocomu.study.domain.QStudyWorkbook.studyWorkbook;
import static co.kr.cocomu.study.domain.QWorkbook.workbook;
import static co.kr.cocomu.user.domain.QUser.user;

import co.kr.cocomu.codingspace.domain.CodingSpaceTab;
import co.kr.cocomu.codingspace.domain.QCodingSpaceTab;
import co.kr.cocomu.codingspace.domain.vo.TabStatus;
import co.kr.cocomu.codingspace.dto.response.AllTabDto;
import co.kr.cocomu.codingspace.dto.response.UserDto;
import co.kr.cocomu.codingspace.repository.query.CodingSpaceTabQuery;
import co.kr.cocomu.study.dto.response.WorkbookDto;
import co.kr.cocomu.user.domain.QUser;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CodingSpaceTabQueryImpl implements CodingSpaceTabQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, List<UserDto>> findUsersBySpace(final List<Long> spaceIds) {
        return queryFactory.select(codingSpaceTab.codingSpace.id, user)
            .from(codingSpaceTab)
            .join(user).on(codingSpaceTab.user.id.eq(user.id))
            .where(codingSpaceTab.codingSpace.id.in(spaceIds))
            .transform(GroupBy.groupBy(codingSpaceTab.codingSpace.id)
                .as(GroupBy.list(
                    Projections.fields(
                        UserDto.class,
                        user.id.as("id"),
                        user.nickname.as("nickname"),
                        user.profileImageUrl.as("profileImageUrl"),
                        codingSpaceTab.role.as("role")
                    )
                ))
            );
    }

    public List<UserDto> findUsers(final Long codingSpaceId) {
        return queryFactory
            .select(Projections.fields(UserDto.class,
                codingSpaceTab.user.id,
                codingSpaceTab.user.nickname,
                codingSpaceTab.user.profileImageUrl,
                codingSpaceTab.role
            ))
            .from(codingSpaceTab)
            .where(
                codingSpaceTab.codingSpace.id.eq(codingSpaceId),
                codingSpaceTab.status.eq(TabStatus.ACTIVE)
            )
            .fetch();
    }

    @Override
    public List<AllTabDto> findAllTabs(final Long codingSpaceId) {
        return queryFactory
            .select(Projections.fields(
                AllTabDto.class,
                codingSpaceTab.id.as("tabId"),
                codingSpaceTab.documentKey.as("documentKey"),
                codingSpaceTab.user.id.as("userId"),
                codingSpaceTab.user.nickname.as("nickname"),
                codingSpaceTab.user.profileImageUrl.as("profileImageUrl"),
                codingSpaceTab.role.as("role")
            ))
            .from(codingSpaceTab)
            .join(codingSpaceTab.user, user)
            .where(
                codingSpaceTab.codingSpace.id.eq(codingSpaceId),
                codingSpaceTab.status.eq(TabStatus.ACTIVE)
            )
            .fetch();
    }

}
