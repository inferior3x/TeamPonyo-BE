package com.econovation.teamponyo.domains.user.application.port.in;

import com.econovation.teamponyo.domains.user.application.port.in.dto.FormPersonalUserRegisterCommand;
import com.econovation.teamponyo.domains.user.application.port.in.dto.FormTeamUserRegisterCommand;

public interface FormUserRegisterUseCase {
    //TODO: Valid
    void register(FormPersonalUserRegisterCommand command);
    void register(FormTeamUserRegisterCommand command);
}
