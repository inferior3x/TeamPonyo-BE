package com.econovation.teamponyo.domains.user.query.port.out;

import com.econovation.teamponyo.domains.user.query.port.in.dto.MemberProfileDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.SearchedUserDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.UserSearchReq;
import com.econovation.teamponyo.domains.user.query.model.UserDocument;
import java.util.List;
import java.util.Optional;

public interface UserSearchPort {
    List<SearchedUserDTO> search(UserSearchReq req);
    Optional<UserDocument> findById(Long userId);
    default UserDocument getById(Long userId){
        return findById(userId).orElseThrow(()->new IllegalArgumentException("유저가 없음"));
    }
    void save(UserDocument userDocument);
    void addTeamId(Long userId, Long teamId);
    void removeTeamId(Long userId, Long teamId);
    List<MemberProfileDTO> getFollowerProfiles(Long userId);
    List<MemberProfileDTO> getFollowingProfiles(Long userId);
}
