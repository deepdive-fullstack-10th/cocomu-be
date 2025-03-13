package co.kr.cocomu.study.repository.query.impl;

import static co.kr.cocomu.study.domain.QStudyUser.studyUser;
import static co.kr.cocomu.user.domain.QUser.user;

import co.kr.cocomu.study.domain.vo.StudyRole;
import co.kr.cocomu.study.domain.vo.StudyUserStatus;
import co.kr.cocomu.study.dto.request.StudyUserFilterDto;
import co.kr.cocomu.study.dto.response.LeaderDto;
import co.kr.cocomu.study.dto.response.StudyMemberDto;
import co.kr.cocomu.study.repository.query.StudyUserQueryRepository;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudyUserRepositoryImpl implements StudyUserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Map<Long, LeaderDto> findLeaderByStudies(final List<Long> studyIds) {
        return queryFactory.select(studyUser.study.id, user)
            .from(studyUser)
            .join(user).on(studyUser.user.id.eq(user.id))
            .where(
                studyUser.study.id.in(studyIds),
                studyUser.role.eq(StudyRole.LEADER),
                studyUser.status.eq(StudyUserStatus.JOIN)
            )
            .transform(GroupBy.groupBy(studyUser.study.id).as(
                Projections.fields(
                    LeaderDto.class,
                    user.id.as("id"),
                    user.nickname.as("nickname"),
                    user.profileImageUrl.as("profileImageUrl")
                )
            ));
    }

    public LeaderDto findLeaderByStudyId(final Long studyId) {
        return queryFactory.select(Projections.fields(
                LeaderDto.class,
                user.id.as("id"),
                user.nickname.as("nickname"),
                user.profileImageUrl.as("profileImageUrl")
            ))
            .from(studyUser)
            .join(user).on(studyUser.user.id.eq(user.id))
            .where(
                studyUser.study.id.eq(studyId),
                studyUser.role.eq(StudyRole.LEADER),
                studyUser.status.eq(StudyUserStatus.JOIN)
            )
            .fetchOne();
    }

    @Override
    public List<StudyMemberDto> findMembers(final Long studyId, final StudyUserFilterDto filter) {
        return queryFactory.select(Projections.fields(
                StudyMemberDto.class,
                studyUser.id.as("studyUserId"),
                user.id.as("userId"),
                user.nickname.as("nickname"),
                user.profileImageUrl.as("profileImageUrl"),
                studyUser.role.as("role"),
                studyUser.createdAt.as("joinedDate")
            ))
            .from(studyUser)
            .join(user).on(studyUser.user.id.eq(user.id))
            .where(
                studyUser.study.id.eq(studyId),
                studyUser.status.eq(StudyUserStatus.JOIN),
                getSelectCondition(filter.lastNickname(), filter.lastIndex())
            )
            .orderBy(getOrderSpecifiers(filter.lastNickname()))
            .limit(20)
            .fetch();
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(final String lastNickname) {
        if (lastNickname == null || lastNickname.isEmpty()) {
            return new OrderSpecifier[] {
                studyUser.role.when(StudyRole.LEADER).then(0).otherwise(1).asc(),
                user.nickname.asc(),
                studyUser.id.asc()
            };
        }

        return new OrderSpecifier[]{ user.nickname.asc(), studyUser.id.asc() };
    }

    private BooleanExpression getSelectCondition(final String lastNickname, final Long lastIndex) {
        if ((lastNickname == null || lastNickname.isEmpty()) && lastIndex == null) {
            return null;
        }

        if (lastIndex == null) {
            return user.nickname.gt(lastNickname).and(studyUser.role.eq(StudyRole.MEMBER));
        }

        if (lastNickname == null) {
            return studyUser.id.gt(lastIndex).and(studyUser.role.eq(StudyRole.MEMBER));
        }

        return (user.nickname.gt(lastNickname)
            .or(user.nickname.eq(lastNickname).and(studyUser.id.gt(lastIndex))))
            .and(studyUser.role.eq(StudyRole.MEMBER));
    }

}
