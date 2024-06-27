package com.econovation.teamponyo.domains.user.application.service;

import com.econovation.teamponyo.domains.user.application.port.in.UserChangePasswordUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.dto.UserChangePasswordCommand;
import com.econovation.teamponyo.domains.user.application.port.out.UserLoadPort;
import com.econovation.teamponyo.domains.user.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserChangePasswordUseCase {
    private final UserLoadPort userLoadPort;

    @Transactional
    @Override
    public void changePassword(@Valid UserChangePasswordCommand command) {
        User user = userLoadPort.getByUserId(command.userId());
        user.changePassword(command.oldPassword(), command.newPassword());
    }
}
