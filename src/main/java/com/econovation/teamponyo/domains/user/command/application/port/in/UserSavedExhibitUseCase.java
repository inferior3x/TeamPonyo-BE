package com.econovation.teamponyo.domains.user.command.application.port.in;

//TODO: 이걸 여기다 두는게 맞나? 그리고 추가 삭제 같이 두는게 맞나?
public interface UserSavedExhibitUseCase {
    void saveExhibit(Long exhibitId);
    void removeSavedExhibit(Long exhibitId);
}
