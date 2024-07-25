package com.econovation.teamponyo.domains.exhibit.query.port.in;

import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesReq;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesDTO;

public interface ExhibitSummariesQuery {
    ExhibitSummariesDTO get(ExhibitSummariesReq req);
}