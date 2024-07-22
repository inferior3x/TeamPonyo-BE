package com.econovation.teamponyo.domains.exhibit.query.port.in;

import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitDetailsDTO;

public interface ExhibitDetailsQuery {
    ExhibitDetailsDTO findById(Long exhibitId);
}
