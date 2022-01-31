package ua.com.alevel.service.impl;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import ua.com.alevel.elastic.document.ImageIndex;
import ua.com.alevel.service.ElasticImageSearchService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ElasticImageSearchServiceImpl implements ElasticImageSearchService {

    private static final String IMAGE_INDEX = "imageindex";

    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticImageSearchServiceImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public List<String> searchImageGameName(String query) {
        QueryBuilder queryBuilder = QueryBuilders
                .wildcardQuery("nameGame", "*" + query.toLowerCase() + "*");
        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .withPageable(PageRequest.of(0, 5))
                .build();
        SearchHits<ImageIndex> searchSuggestions =
                elasticsearchOperations.search(searchQuery,
                        ImageIndex.class,
                        IndexCoordinates.of(IMAGE_INDEX));
        final List<String> suggestions = new ArrayList<>();
        searchSuggestions.getSearchHits().forEach(searchHit -> suggestions.add(searchHit.getContent().getNameGame()));
        return suggestions;
    }
}
