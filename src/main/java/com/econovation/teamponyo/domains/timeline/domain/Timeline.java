package com.econovation.teamponyo.domains.timeline.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Timeline {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long timelineId;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = true)
    private Long teamId;
    @Column(nullable = false)
    private String teamName;

    public void validateIsMine(Long userId){
        if (!userId.equals(this.userId))
            throw new IllegalArgumentException("나의 타임라인이 아님");
    }

    public static Timeline createWithTeam(Long userId, Long teamId, String teamName){
            if (userId.equals(teamId))
                throw new IllegalArgumentException("자신을 추가할 수 없음");
            return Timeline.builder()
                        .userId(userId)
                        .teamId(teamId)
                        .teamName(teamName)
                        .build();
    }
    public static Timeline createWithoutTeam(Long userId, String teamName){
            return Timeline.builder()
                    .userId(userId)
                    .teamName(teamName)
                    .build();
    }
}
