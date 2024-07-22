package com.econovation.teamponyo.domains.user.command.application.port.in;

public interface UserVisitedExhibitUseCase {
    void addVisitedExhibit(Long exhibitId);
    void removeVisitedExhibit(Long exhibitId);
}
