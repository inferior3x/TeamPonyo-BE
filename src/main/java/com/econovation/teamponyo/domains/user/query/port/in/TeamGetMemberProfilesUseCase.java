package com.econovation.teamponyo.domains.user.query.port.in;

import com.econovation.teamponyo.domains.user.query.port.in.dto.MemberProfileDTO;
import java.util.List;

public interface TeamGetMemberProfilesUseCase {
    List<MemberProfileDTO> getMembers(Long teamId);
}
