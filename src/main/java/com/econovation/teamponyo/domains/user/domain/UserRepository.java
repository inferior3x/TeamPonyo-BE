package com.econovation.teamponyo.domains.user.domain;

import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import com.econovation.teamponyo.domains.user.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialLoginInfo(SocialLoginInfo socialLoginInfo);
    Optional<User> findByFormCredentialsLoginId(String loginId);
    boolean existsBySocialLoginInfo(SocialLoginInfo socialLoginInfo);
}
