package com.econovation.teamponyo.domains.user.command.application.service;

import com.econovation.teamponyo.common.interfaces.RequesterInfo;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserChangeIntroductionUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserChangePasswordUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserSavedExhibitUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserVisitedExhibitUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.UserChangePasswordCommand;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserLoadRepository;
import com.econovation.teamponyo.domains.user.query.port.out.UserQueryDAO;
import com.econovation.teamponyo.domains.user.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserInfoService implements
        UserChangePasswordUseCase,
        UserChangeIntroductionUseCase{
    private final UserLoadRepository userLoadRepository;
    private final UserQueryDAO userQueryDAO;
    private final RequesterInfo requesterInfo;

    @Transactional
    @Override
    public void changePassword(@Valid UserChangePasswordCommand command) {
        User user = userLoadRepository.getByUserId(requesterInfo.getUserId());
        user.changePassword(command.oldPassword(), command.newPassword());
    }

    @Transactional
    @Override
    public void changeIntroduction(String introduction) {
        User user = userLoadRepository.getByUserId(requesterInfo.getUserId());
        user.changeIntroduction(introduction);
    }

    private void validateExistingExhibit(Long exhibitId){
        if (!userQueryDAO.existsExhibitById(exhibitId))
            throw new IllegalArgumentException("없는 전시");
    }
}
