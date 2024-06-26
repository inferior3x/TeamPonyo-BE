package com.econovation.teamponyo.domains.user.application.port.out;

import com.econovation.teamponyo.domains.user.domain.model.FormLoginCredentials;
import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import com.econovation.teamponyo.domains.user.domain.model.User;
import java.util.Optional;

public interface UserLoadPort {
    boolean existsByUserId(Long userId);
    boolean existsBySocialLoginInfo(SocialLoginInfo socialLoginInfo);
    Optional<User> findBySocialLoginInfo(SocialLoginInfo socialLoginInfo);
    Optional<User> findByFormLoginCredentials(FormLoginCredentials formLoginCredentials);
    Optional<User> findByUserId(Long userId);
    default User getByUserId(Long userId){
        return findByUserId(userId).orElseThrow(()->new IllegalArgumentException("없는 유저"));
    }
}
