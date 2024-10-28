package com.econovation.teamponyo.common.interfaces;

import java.util.Optional;

/*
현재 요청한 유저의 정보를 얻는 인터페이스
 */
public interface RequesterInfo {
    Optional<Long> findUserId();
    default Long getUserId(){
        return findUserId().orElseThrow(()-> new IllegalArgumentException("알 수 없는 유저입니다."));
    }
}