package com.econovation.teamponyo.domains.exhibit.command.application.port.in.dto;

import com.econovation.teamponyo.common.enums.ExhibitStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(SnakeCaseStrategy.class)
public record ExhibitDetailsDTO(
		Long teamId,
    ExhibitStatus exhibitStatus,
		Boolean editable,
		String posterUrl,
		String title,
		String teamName,
		String address,
		@JsonProperty("position")
		CoordinateDTO coordinate,
		String period,
		String openTimes,
		String fee,
		String contact,
		String description,
		Boolean saved,
		Boolean visited,
		List<String> photoUrls
){}
