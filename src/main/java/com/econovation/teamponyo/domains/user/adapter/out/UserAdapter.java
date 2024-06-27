package com.econovation.teamponyo.domains.user.adapter.out;

import com.econovation.teamponyo.domains.user.application.port.out.UserLoadPort;
import com.econovation.teamponyo.domains.user.application.port.out.UserRecordPort;
import com.econovation.teamponyo.domains.user.domain.UserRepository;
import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import com.econovation.teamponyo.domains.user.domain.model.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserLoadPort, UserRecordPort {
    private final UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
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
