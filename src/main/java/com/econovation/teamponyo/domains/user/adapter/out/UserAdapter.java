package com.econovation.teamponyo.domains.user.adapter.out;

import com.econovation.teamponyo.domains.user.application.port.out.UserLoadPort;
import com.econovation.teamponyo.domains.user.application.port.out.UserRecordPort;
import com.econovation.teamponyo.domains.user.domain.TeamInfoRepository;
import com.econovation.teamponyo.domains.user.domain.UserRepository;
import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import com.econovation.teamponyo.domains.user.domain.model.TeamInfo;
import com.econovation.teamponyo.domains.user.domain.model.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserLoadPort, UserRecordPort {
    private final UserRepository userRepository;
    private final TeamInfoRepository teamInfoRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void saveTeam(User user) {
        if (user.getTeamInfo() == null)
            throw new IllegalArgumentException("팀 설정이 null입니다.");
        TeamInfo teamInfo = user.getTeamInfo();
        user.setTeamInfo(null);
        userRepository.save(user);
        teamInfo.setUser(user);
        teamInfoRepository.save(teamInfo);
        user.setTeamInfo(teamInfo);
    }

    @Override
    public Optional<User> findByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public boolean existsBySocialLoginInfo(SocialLoginInfo socialLoginInfo) {
        return userRepository.existsBySocialLoginInfo(socialLoginInfo);
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return userRepository.findByFormCredentialsLoginId(loginId);
    }

    @Override
    public Optional<User> findBySocialLoginInfo(SocialLoginInfo socialLoginInfo) {
        return userRepository.findBySocialLoginInfo(socialLoginInfo);
    }
}
