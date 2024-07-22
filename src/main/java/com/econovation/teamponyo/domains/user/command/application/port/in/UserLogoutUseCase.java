package com.econovation.teamponyo.domains.user.command.application.port.in;

public interface UserLogoutUseCase {
    void logout(String refreshToken);
}
