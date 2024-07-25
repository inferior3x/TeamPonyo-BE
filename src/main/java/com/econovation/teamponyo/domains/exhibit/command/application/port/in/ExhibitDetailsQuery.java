package com.econovation.teamponyo.domains.exhibit.command.application.port.in;

import com.econovation.teamponyo.domains.exhibit.command.application.port.in.dto.ExhibitDetailsDTO;

public interface ExhibitDetailsQuery {
    ExhibitDetailsDTO findById(Long exhibitId);
}
