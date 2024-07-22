package com.econovation.teamponyo.domains.user.query;

import com.econovation.teamponyo.common.enums.AccountType;
import com.econovation.teamponyo.common.enums.SocialProvider;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserLoadRepository;
import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import com.econovation.teamponyo.domains.user.domain.model.User;
import com.econovation.teamponyo.domains.user.query.port.in.TeamGetMemberProfilesUseCase;
import com.econovation.teamponyo.domains.user.query.port.in.UserGetUserProfileUseCase;
import com.econovation.teamponyo.domains.user.query.port.in.UserOAuth2ExistsUseCase;
import com.econovation.teamponyo.domains.user.query.port.in.UserSearchUseCase;
import com.econovation.teamponyo.domains.user.query.port.in.dto.MemberProfileDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.SearchedUserDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.UserProfileDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.UserSearchReq;
import com.econovation.teamponyo.domains.user.query.port.out.UserQueryDAO;
import com.econovation.teamponyo.domains.user.query.port.out.UserSearchPort;
import com.econovation.teamponyo.infrastructure.s3.S3Uploader;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserQueryService implements
        UserOAuth2ExistsUseCase,
        UserGetUserProfileUseCase,
        UserSearchUseCase,
        TeamGetMemberProfilesUseCase
{
    private final UserQueryDAO userQueryDAO;
    private final UserLoadRepository userLoadRepository;
    private final UserSearchPort userSearchPort;
    //Query에서 S3Uploader 같은거 써도 되는걸까
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    @Override
    public boolean exists(SocialProvider socialProvider, String socialId) {
        return userLoadRepository.existsBySocialLoginInfo(new SocialLoginInfo(socialProvider, socialId));
    }


    @Transactional(readOnly = true)
    @Override
    public UserProfileDTO get(Long userId) {
        User user = userLoadRepository.getByUserId(userId);

        return new UserProfileDTO(
                userId,
                user.isSocialLoginUser() ? user.getUserInfo().getEmail() : user.getFormCredentials().getLoginId(),
                user.getUserInfo().getNickname(),
                s3Uploader.getPublicUrl(user.getProfileKeyName()),
                user.getAccountType(),
                user.getUserInfo().getIntroduction()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberProfileDTO> getMembers(Long teamId) {
        User team = userLoadRepository.getByUserId(teamId);
        team.validateIsTeam();
        return userQueryDAO.getMemberProfiles(teamId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SearchedUserDTO> search(@Valid UserSearchReq req) {
        if (req.accountType() == AccountType.TEAM && req.teamId() != null)
            throw new IllegalArgumentException("유저를 팀으로 검색할 때 팀의 아이디를 지정할 수 없습니다.");
        return userSearchPort.search(req);
    }
}
