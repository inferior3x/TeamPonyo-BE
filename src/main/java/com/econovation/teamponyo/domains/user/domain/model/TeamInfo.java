package com.econovation.teamponyo.domains.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamInfo {
    @Id
    private Long teamId;
    @MapsId
    @OneToOne(mappedBy="teamInfo")
    @JoinColumn(name = "team_id")
    private User user;

    @Column(nullable = false)
    private String representativeName;
    @Column(nullable = false)
    private String evidenceUrl;
    private boolean approval;

    private TeamInfo(String representativeName, String evidenceUrl) {
        this.representativeName = representativeName;
        this.evidenceUrl = evidenceUrl;
        this.approval = false;
    }

    public static TeamInfo createTeamInfo(String representativeName, String evidenceUrl){
        return new TeamInfo(representativeName, evidenceUrl);
    }

}
