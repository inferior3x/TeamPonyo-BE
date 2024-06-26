package com.econovation.teamponyo.domains.user.application.port.in;

public interface UserLogoutUseCase {
    void logout(String refreshToken);
}
