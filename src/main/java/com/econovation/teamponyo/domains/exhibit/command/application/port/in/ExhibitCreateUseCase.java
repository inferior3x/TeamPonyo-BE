package com.econovation.teamponyo.domains.exhibit.command.application.port.in;

import com.econovation.teamponyo.domains.exhibit.command.application.port.in.dto.ExhibitCreateCommand;

public interface ExhibitCreateUseCase {
    Long create(ExhibitCreateCommand command);
}
