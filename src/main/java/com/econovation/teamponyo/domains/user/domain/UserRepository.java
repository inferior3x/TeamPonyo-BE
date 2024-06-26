package com.econovation.teamponyo.domains.user.domain;

import com.econovation.teamponyo.domains.user.domain.model.FormLoginCredentials;
import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import com.econovation.teamponyo.domains.user.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialLoginInfo(SocialLoginInfo socialLoginInfo);
    Optional<User> findByFormLoginCredentials(FormLoginCredentials formLoginCredentials);
    boolean existsBySocialLoginInfo(SocialLoginInfo socialLoginInfo);
}
