package com.econovation.teamponyo.domains.user.query.adapter.out;

import com.econovation.teamponyo.domains.user.domain.model.User;
import com.econovation.teamponyo.domains.user.query.port.in.dto.MemberProfileDTO;
import com.econovation.teamponyo.domains.user.query.port.out.UserQueryDAO;
import com.econovation.teamponyo.infrastructure.s3.S3Uploader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryDAO {
    @PersistenceContext
    private EntityManager entityManager;
    private final S3Uploader s3Uploader;

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

    @Override
    public List<MemberProfileDTO> getMemberProfiles(Long teamId) {
        String jpql = "SELECT u FROM TeamMember tm JOIN tm.member u WHERE tm.team.userId = :teamId";
        List<User> members = entityManager.createQuery(jpql, User.class)
                .setParameter("teamId", teamId)
                .getResultList();

        return members.stream()
                .map(member -> {

                    return new MemberProfileDTO(
                            member.getUserId(),
                            member.isSocialLoginUser() ? member.getSocialLoginInfo().getSocialId() : member.getFormCredentials().getLoginId(),
                            member.getAccountType(),
                            s3Uploader.getPublicUrl(member.getProfileKeyName()),
                            member.getUserInfo().getNickname(),
                            member.getUserInfo().getIntroduction(),
                            false
                    );
                })
                .toList();
    }
}
