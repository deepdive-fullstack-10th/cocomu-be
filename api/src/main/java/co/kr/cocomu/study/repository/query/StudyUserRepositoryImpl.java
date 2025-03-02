package co.kr.cocomu.study.repository.query;

import static co.kr.cocomu.study.domain.QStudyUser.studyUser;
import static co.kr.cocomu.user.domain.QUser.user;

import co.kr.cocomu.study.controller.query.StudyUserQueryRepository;
import co.kr.cocomu.study.domain.vo.StudyRole;
import co.kr.cocomu.study.domain.vo.StudyUserStatus;
import co.kr.cocomu.study.dto.response.LeaderDto;
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

    public Map<Long, LeaderDto> findAllLeaderByStudies(final List<Long> studyIds) {
        return queryFactory.select(studyUser.study.id, user)
            .from(studyUser)
            .join(user).on(studyUser.user.id.eq(user.id))
            .where(
                studyUser.study.id.in(studyIds),
                studyUser.studyRole.eq(StudyRole.LEADER),
                studyUser.studyUserStatus.eq(StudyUserStatus.JOIN)
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
                studyUser.studyRole.eq(StudyRole.LEADER),
                studyUser.studyUserStatus.eq(StudyUserStatus.JOIN)
            )
            .fetchOne();
    }

}
