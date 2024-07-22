package com.econovation.teamponyo.domains.user.domain.model;

import com.econovation.teamponyo.common.enums.TeamMemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"team_id", "member_id"})
})
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long teamMemberId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    User team;

    @ManyToOne
    @JoinColumn(name = "member_id")
    User member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TeamMemberRole role;

    public static TeamMember of(User team, User member){
        return TeamMember.builder()
                .team(team)
                .member(member)
                .role(TeamMemberRole.NORMAL)
                .build();
    }

}
