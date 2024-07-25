package com.econovation.teamponyo.domains.exhibit.query.port.in;

import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitBannerDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitBannerReq;
import java.util.List;

public interface ExhibitBannerQuery {
    List<ExhibitBannerDTO> get(ExhibitBannerReq req);
}
