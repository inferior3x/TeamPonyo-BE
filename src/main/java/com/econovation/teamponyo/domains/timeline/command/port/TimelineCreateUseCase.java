package com.econovation.teamponyo.domains.timeline.command.port;

public interface TimelineCreateUseCase {
    //검색을 통해 팀 아이디로 추가하는 경우
    Long create(Long teamId);
    //팀을 찾을 수 없을 때 직접 입력하여 추가하는 경우
    Long create(String teamName);
}
