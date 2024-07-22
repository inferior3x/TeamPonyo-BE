package com.econovation.teamponyo.domains.user.query.port.in;

import com.econovation.teamponyo.domains.user.query.port.in.dto.SearchedUserDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.UserSearchReq;
import java.util.List;

public interface UserSearchUseCase {
    List<SearchedUserDTO> search(UserSearchReq req);
}
