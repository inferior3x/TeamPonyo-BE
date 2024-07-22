package com.econovation.teamponyo.domains.follow.query;

import com.econovation.teamponyo.common.interfaces.RequesterInfo;
import com.econovation.teamponyo.domains.follow.query.dto.res.FollowInfo;
import com.econovation.teamponyo.domains.user.query.port.in.dto.MemberProfileDTO;
import com.econovation.teamponyo.domains.user.query.port.out.UserSearchPort;
import com.econovation.teamponyo.domains.user.query.model.UserDocument;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowQueryService {
    private final RequesterInfo requesterInfo;
    private final UserSearchPort userSearchPort;
    public FollowInfo getFollowInfo(Long userId){
        UserDocument userDocument = userSearchPort.getById(userId);

        Boolean followed = null;
        Optional<Long> userIdOpt = requesterInfo.findUserId();

        if (userIdOpt.isPresent()) {
            Long requesterId = userIdOpt.get();
            if (!Objects.equals(userId, requesterId))
                followed = userDocument.getFollowers().contains(requesterId);
        } else
            followed = false;

        return new FollowInfo(userDocument.getFollowerNumber(), userDocument.getFollowingNumber(), followed);
    }

    public List<MemberProfileDTO> getFollowerProfiles(Long userId){
        return userSearchPort.getFollowerProfiles(userId);
    }
    public List<MemberProfileDTO> getFollowingProfiles(Long userId){
        return userSearchPort.getFollowingProfiles(userId);
    }
}
