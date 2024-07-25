package com.econovation.teamponyo.domains.exhibit.query.port.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

//정렬 전략 선택할 수도 있으니 DTO로 만들었음.
@JsonNaming(SnakeCaseStrategy.class)
public record ExhibitBannerReq(Integer number){}
