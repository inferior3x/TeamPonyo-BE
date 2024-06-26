package com.econovation.teamponyo.domains.user.application.port.out;

import com.econovation.teamponyo.domains.user.domain.model.User;

public interface UserRecordPort {
    void save(User user);
}
