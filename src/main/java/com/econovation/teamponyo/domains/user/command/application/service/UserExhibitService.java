package com.econovation.teamponyo.domains.user.command.application.service;

import com.econovation.teamponyo.common.interfaces.RequesterInfo;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserSavedExhibitUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserVisitedExhibitUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserLoadRepository;
import com.econovation.teamponyo.domains.user.domain.model.User;
import com.econovation.teamponyo.domains.user.query.port.out.UserQueryDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserExhibitService implements
            UserSavedExhibitUseCase,
            UserVisitedExhibitUseCase {
        private final UserLoadRepository userLoadRepository;
        private final UserQueryDAO userQueryDAO;
        private final RequesterInfo requesterInfo;


        //TODO: Exhibit과 연관되어있으니 도메인 서비스로 빼야할듯.
        @Transactional
        @Override
        public void saveExhibit(Long exhibitId) {
            validateExistingExhibit(exhibitId);
            User user = userLoadRepository.getByUserId(requesterInfo.getUserId());
            user.saveExhibit(exhibitId);
        }

        @Transactional
        @Override
        public void removeSavedExhibit(Long exhibitId) {
            validateExistingExhibit(exhibitId);
            User user = userLoadRepository.getByUserId(requesterInfo.getUserId());
            user.removeSavedExhibit(exhibitId);

        }

        @Transactional
        @Override
        public void addVisitedExhibit(Long exhibitId) {
            System.out.println("service" + exhibitId);
            validateExistingExhibit(exhibitId);
            User user = userLoadRepository.getByUserId(requesterInfo.getUserId());
            user.addVisitedExhibit(exhibitId);
        }

        @Transactional
        @Override
        public void removeVisitedExhibit(Long exhibitId) {
            validateExistingExhibit(exhibitId);
            User user = userLoadRepository.getByUserId(requesterInfo.getUserId());
            user.removeVisitedExhibit(exhibitId);
        }

        private void validateExistingExhibit(Long exhibitId){
            if (!userQueryDAO.existsExhibitById(exhibitId))
                throw new IllegalArgumentException("없는 전시");
        }
    }