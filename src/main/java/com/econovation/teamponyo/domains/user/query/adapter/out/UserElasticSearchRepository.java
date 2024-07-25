package com.econovation.teamponyo.domains.user.query.adapter.out;

import com.econovation.teamponyo.domains.user.query.model.UserDocument;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserElasticSearchRepository extends ElasticsearchRepository<UserDocument, Long>{
    List<UserDocument> getAllByTeamIdsContains(Long userId);
}