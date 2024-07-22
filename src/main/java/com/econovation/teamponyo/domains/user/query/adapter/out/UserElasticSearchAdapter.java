package com.econovation.teamponyo.domains.user.query.adapter.out;

import static com.econovation.teamponyo.common.consts.CommonStatics.DEFAULT_PROFILE_IMAGE_KEYNAME;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import co.elastic.clients.elasticsearch.core.MgetRequest;
import co.elastic.clients.elasticsearch.core.MgetResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.get.GetResult;
import co.elastic.clients.elasticsearch.core.mget.MultiGetResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.econovation.teamponyo.domains.user.query.model.UserDocument;
import com.econovation.teamponyo.domains.user.query.port.in.dto.MemberProfileDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.SearchedUserDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.UserSearchReq;
import com.econovation.teamponyo.domains.user.query.port.out.UserSearchPort;
import com.econovation.teamponyo.infrastructure.s3.S3Uploader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserElasticSearchAdapter implements UserSearchPort {
    private final UserElasticSearchRepository userElasticSearchRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final S3Uploader s3Uploader;
    private static final String INDEX = "users";

    @Override
    public List<MemberProfileDTO> getFollowerProfiles(Long userId) {
        UserDocument userDocument = getById(userId);
        Set<Long> followerIds = userDocument.getFollowers();
        return getMemberProfilesByIds(followerIds);
    }

    @Override
    public List<MemberProfileDTO> getFollowingProfiles(Long userId) {
        UserDocument userDocument = getById(userId);
        Set<Long> followingIds = userDocument.getFollowings();
        return getMemberProfilesByIds(followingIds);
    }

    private List<MemberProfileDTO> getMemberProfilesByIds(Set<Long> ids) {
        if (ids.isEmpty())
            return List.of();
        try {
            MgetRequest request = MgetRequest.of(m -> m
                    .index(INDEX)
                    .ids(ids.stream().map(String::valueOf).toList())
            );
            MgetResponse<UserDocument> response = elasticsearchClient.mget(request, UserDocument.class);

            System.out.println("response.docs().size() = " + response.docs().size());

            return response.docs().stream().parallel()
                    .map(MultiGetResponseItem::result)
                    .filter(GetResult::found)
                    .map(GetResult::source)
                    .map(this::toMemberProfileDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch profiles", e);
        }
    }

    private MemberProfileDTO toMemberProfileDTO(UserDocument userDocument) {
        return new MemberProfileDTO(
                userDocument.getUserId(),
                userDocument.getLoginId(),
                userDocument.getAccountType(),
                s3Uploader.getPublicUrl(userDocument.getProfileImageKeyName()),
                userDocument.getNickname(),
                userDocument.getIntroduction(),
                false
        );
    }

    @Override
    public List<SearchedUserDTO> search(UserSearchReq req) {
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        if (req.nicknameOrLoginId() != null) {
            boolQueryBuilder
                    .should(WildcardQuery.of(w -> w.field("nickname").value("*" + req.nicknameOrLoginId() + "*"))._toQuery())
                    .should(WildcardQuery.of(w -> w.field("loginId").value("*" + req.nicknameOrLoginId() + "*"))._toQuery()).minimumShouldMatch("1");
        }
        if (req.teamId() != null)
            boolQueryBuilder.must(TermQuery.of(t -> t.field("teamIds").value(req.teamId()))._toQuery());
        if (req.accountType() != null)
            boolQueryBuilder.must(MatchQuery.of(t -> t.field("accountType").query(req.accountType().toString()))._toQuery());

        Query boolQuery = boolQueryBuilder.build()._toQuery();

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX)
                .query(boolQuery));

        SearchResponse<UserDocument> searchResponse;

        try {
            searchResponse = elasticsearchClient.search(searchRequest, UserDocument.class);
        } catch (IOException e) {
            throw new RuntimeException("도큐먼트 찾기 오류", e);
        }

        File defaultImage = s3Uploader.downloadFile(DEFAULT_PROFILE_IMAGE_KEYNAME);

        return searchResponse.hits().hits().stream()
                .parallel()
                .map(Hit::source)
                .map(doc ->
                    new SearchedUserDTO(
                            doc.getUserId(),
                            doc.getNickname(),
                            doc.getLoginId(),
                            s3Uploader.getPublicUrl(doc.getProfileImageKeyName()),
                            doc.getAccountType()
                    )
                ).toList();
    }

    @Override
    public Optional<UserDocument> findById(Long userId) {
        return userElasticSearchRepository.findById(userId);
    }

    @Override
    public void save(UserDocument userDocument) {
        userElasticSearchRepository.save(userDocument);
    }

    @Override
    public void addTeamId(Long userId, Long teamId) {
        userElasticSearchRepository.findById(userId).ifPresent(doc -> {
            List<Long> teamIds = doc.getTeamIds();
            if (!teamIds.contains(teamId)) {
                teamIds.add(teamId);
                userElasticSearchRepository.save(doc);
            }
        });
    }

    @Override
    public void removeTeamId(Long userId, Long teamId) {
        userElasticSearchRepository.findById(userId).ifPresent(doc -> {
            List<Long> teamIds = doc.getTeamIds();
            if (teamIds.contains(teamId)) {
                teamIds.remove(teamId);
                userElasticSearchRepository.save(doc);
            }
        });
    }
}