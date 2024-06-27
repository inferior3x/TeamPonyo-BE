package com.econovation.teamponyo.domains.user.application.service;

import com.econovation.teamponyo.domains.user.application.port.in.UserChangePasswordUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.dto.UserChangePasswordCommand;
import com.econovation.teamponyo.domains.user.application.port.out.UserLoadPort;
import com.econovation.teamponyo.domains.user.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserChangePasswordUseCase {
    private final UserLoadPort userLoadPort;
    @Override
    public void changePassword(Long userId, @Valid UserChangePasswordCommand command) {
        User user = userLoadPort.getByUserId(userId);
        user.changePassword(command.oldPassword(), command.newPassword());
    }
}
