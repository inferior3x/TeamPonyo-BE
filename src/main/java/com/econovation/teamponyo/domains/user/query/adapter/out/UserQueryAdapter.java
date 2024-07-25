package com.econovation.teamponyo.domains.user.query.adapter.out;

import com.econovation.teamponyo.common.interfaces.RequesterInfo;
import com.econovation.teamponyo.domains.follow.domain.Follow;
import com.econovation.teamponyo.domains.follow.domain.QFollow;
import com.econovation.teamponyo.domains.user.domain.model.QTeamMember;
import com.econovation.teamponyo.domains.user.domain.model.QUser;
import com.econovation.teamponyo.domains.user.domain.model.User;
import com.econovation.teamponyo.domains.user.query.model.UserDocument;
import com.econovation.teamponyo.domains.user.query.port.in.dto.MemberProfileDTO;
import com.econovation.teamponyo.domains.user.query.port.out.UserQueryDAO;
import com.econovation.teamponyo.infrastructure.s3.S3Uploader;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryDAO {
    @PersistenceContext
    private final EntityManager entityManager;
    private final S3Uploader s3Uploader;
    private final JPAQueryFactory jpaQueryFactory;
    private final RequesterInfo requesterInfo;

    @Override
    public boolean existsExhibitById(Long exhibitId) {
        String sql = "SELECT 1 FROM exhibit WHERE exhibit_id = :exhibitId LIMIT 1";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("exhibitId", exhibitId);
        return !query.getResultList().isEmpty();
    }

    @Override
    public boolean existsTeamMember(Long teamId, Long memberId) {
        String sql = "SELECT 1 FROM team_member WHERE team_id = :teamId AND member_id = :memberId LIMIT 1";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("teamId", teamId);
        query.setParameter("memberId", memberId);
        return !query.getResultList().isEmpty();
    }

    public List<MemberProfileDTO> getMemberProfiles(Long teamId, Long requesterId) {
        QTeamMember qTeamMember = QTeamMember.teamMember;
        QUser qUser = QUser.user;
        QFollow qFollow = QFollow.follow;

        List<User> members = jpaQueryFactory.select(qUser)
                .from(qTeamMember)
                .join(qTeamMember.member, qUser)
                .where(qTeamMember.team.userId.eq(teamId))
                .fetch();

        return members.stream().parallel()
                .map(member -> {
                    boolean isFollowed = false;
                    if (requesterId != null) {
                        System.out.println("asdfasfasdfasdfsd");
                        BooleanExpression followCondition = qFollow.followId.followerId.eq(requesterId)
                                .and(qFollow.followId.followeeId.eq(member.getUserId()));
                        isFollowed = jpaQueryFactory.selectOne()
                                .from(qFollow)
                                .where(followCondition)
                                .fetchFirst() != null;
                    }


                    return new MemberProfileDTO(
                            member.getUserId(),
                            member.isSocialLoginUser() ? member.getSocialLoginInfo().getSocialId() : member.getFormCredentials().getLoginId(),
                            member.getAccountType(),
                            s3Uploader.getPublicUrl(member.getProfileKeyName()),
                            member.getUserInfo().getNickname(),
                            member.getUserInfo().getIntroduction(),
                            isFollowed
                    );
                })
                .toList();
    }
}
