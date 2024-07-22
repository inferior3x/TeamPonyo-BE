package com.econovation.teamponyo.domains.exhibit.domain.model;

import com.econovation.teamponyo.common.enums.ExhibitCategory;
import com.econovation.teamponyo.common.enums.ExhibitStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exhibit{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long exhibitId;

    @Column(nullable = false)
    private Long teamId;

    @Column(nullable = false)
    private String posterKeyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExhibitCategory exhibitCategory;

    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private String openTimes;

    @Column(nullable = false)
    private String fee;

    //밸류
    @Column(nullable = false)
    @Embedded
    private Location location;

    @Column(nullable = false)
    private String contact;

    //TODO: lob
    @Column(nullable = false)
    private String description;

    @Embedded
    private ExhibitPhotos exhibitPhotos;

    @Embedded
    private Period period;

    @Column(nullable = false)
    private int viewCount;

    public boolean isAvailable(){
        return period.isAvailable();
    }

    public ExhibitStatus getExhibitStatus(){
        return this.period.getExhibitStatus();
    }

    public boolean isEditableBy(Long userId){
        return Objects.equals(userId, this.teamId);
    }

    public ExhibitPhotos getExhibitPhotos(){
        return this.exhibitPhotos;
    }

    public static Exhibit create(Long teamId, String posterKeyName, ExhibitCategory exhibitCategory, String title,
            Location location, String openTimes, String fee, String contact, String description, ExhibitPhotos exhibitPhotos, Period period){
        //
        return Exhibit.builder()
                .teamId(teamId)
                .posterKeyName(posterKeyName)
                .exhibitCategory(exhibitCategory)
                .title(title)
                .location(location)
                .openTimes(openTimes)
                .fee(fee)
                .contact(contact)
                .description(description)
                .period(period)
                .viewCount(0)
                .exhibitPhotos(exhibitPhotos)
                .build();
    }

    public void updateExhibitPhotos(ExhibitPhotos exhibitPhotos){
        setExhibitPhotos(exhibitPhotos);
    }
}
