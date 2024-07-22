package com.econovation.teamponyo.domains.user.command.application.port.in;

/**
 * 리프레시 토큰 검증은 애플리케이션에서 수행한다.
 */
public interface TokenReissueUseCase {
    String access(String refreshToken);
    String refresh(String refreshToken);
}
