package com.econovation.teamponyo.domains.exhibit.domain.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ExhibitPhotoCreateDTO(String keyName)
{}

//필요시 내부 ExhibitPhoto dto만드는게 좋음...