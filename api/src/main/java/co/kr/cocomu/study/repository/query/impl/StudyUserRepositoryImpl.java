package co.kr.cocomu.study.repository.query.impl;

import static co.kr.cocomu.study.domain.QStudyUser.studyUser;
import static co.kr.cocomu.user.domain.QUser.user;

import co.kr.cocomu.study.domain.vo.StudyRole;
import co.kr.cocomu.study.domain.vo.StudyUserStatus;
import co.kr.cocomu.study.dto.response.LeaderDto;
import co.kr.cocomu.study.repository.query.StudyUserQueryRepository;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
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

}
