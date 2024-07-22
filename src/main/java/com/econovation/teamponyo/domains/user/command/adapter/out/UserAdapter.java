package com.econovation.teamponyo.domains.user.command.adapter.out;

import com.econovation.teamponyo.domains.user.command.application.port.out.TeamMemberRepository;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserLoadRepository;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserRecordRepository;
import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import com.econovation.teamponyo.domains.user.domain.model.TeamInfo;
import com.econovation.teamponyo.domains.user.domain.model.TeamMember;
import com.econovation.teamponyo.domains.user.domain.model.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserLoadRepository, UserRecordRepository, TeamMemberRepository {
    private final UserJpaRepository userJpaRepository;
    private final TeamInfoJpaRepository teamInfoJpaRepository;
    private final TeamMemberJpaRepository teamMemberJpaRepository;

    @Override
    public void save(TeamMember teamMember) {
        teamMemberJpaRepository.save(teamMember);
    }

    @Override
    public void save(User user) {
        userJpaRepository.save(user);
    }

    @Override
    public void saveTeam(User user) {
        if (user.getTeamInfo() == null)
            throw new IllegalArgumentException("팀 설정이 null입니다.");
        TeamInfo teamInfo = user.getTeamInfo();
        user.setTeamInfo(null);
        userJpaRepository.save(user);
        teamInfo.setUser(user);
        teamInfoJpaRepository.save(teamInfo);
        user.setTeamInfo(teamInfo);
    }

    @Override
    public Optional<User> findByUserId(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return userJpaRepository.existsById(userId);
    }

    @Override
    public boolean existsBySocialLoginInfo(SocialLoginInfo socialLoginInfo) {
        return userJpaRepository.existsBySocialLoginInfo(socialLoginInfo);
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return userJpaRepository.findByFormCredentialsLoginId(loginId);
    }

    @Override
    public Optional<User> findBySocialLoginInfo(SocialLoginInfo socialLoginInfo) {
        return userJpaRepository.findBySocialLoginInfo(socialLoginInfo);
    }

    @Override
    public List<TeamMember> findAllByTeam(User team) {
        return teamMemberJpaRepository.findAllByTeam(team);
    }
}
