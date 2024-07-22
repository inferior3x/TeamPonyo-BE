package com.econovation.teamponyo.domains.exhibit.command.application.port.out;

import com.econovation.teamponyo.domains.exhibit.domain.model.Exhibit;

public interface ExhibitRecordRepository {
    void save(Exhibit exhibit);
}
