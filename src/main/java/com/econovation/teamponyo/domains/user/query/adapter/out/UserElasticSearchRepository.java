package com.econovation.teamponyo.domains.user.query.adapter.out;

import com.econovation.teamponyo.domains.user.query.model.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserElasticSearchRepository extends ElasticsearchRepository<UserDocument, Long> {
}