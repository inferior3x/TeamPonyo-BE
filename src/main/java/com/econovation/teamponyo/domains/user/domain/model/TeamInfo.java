package com.econovation.teamponyo.domains.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter(AccessLevel.PACKAGE)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamInfo {
    @Id
    @Setter
    private Long teamId;

    @Setter
    @MapsId
    @OneToOne//(mappedBy="teamInfo")
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private String representativeName;
    @Column(nullable = false)
    private String evidenceUrl;
    @Column(nullable = false)
    private boolean approval;

    private TeamInfo(String representativeName, String evidenceUrl) {
        this.representativeName = representativeName;
        this.evidenceUrl = evidenceUrl;
        this.approval = false;
    }

    public static TeamInfo create(String representativeName, String evidenceUrl){
        return new TeamInfo(representativeName, evidenceUrl);
    }

}
