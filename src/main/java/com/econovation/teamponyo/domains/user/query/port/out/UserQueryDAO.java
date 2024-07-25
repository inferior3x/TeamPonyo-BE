package com.econovation.teamponyo.domains.user.query.port.out;

import com.econovation.teamponyo.domains.user.query.port.in.dto.MemberProfileDTO;
import jakarta.annotation.Nullable;
import java.util.List;

public interface UserQueryDAO {
    boolean existsExhibitById(Long exhibitId);
    boolean existsTeamMember(Long teamId, Long memberId);
    List<MemberProfileDTO> getMemberProfiles(Long teamId, @Nullable Long requesterInfo);
}
