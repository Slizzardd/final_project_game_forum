package ua.com.alevel.facade.impl;

import org.springframework.stereotype.Service;
import ua.com.alevel.facade.ElasticImageSearchFacade;
import ua.com.alevel.service.ElasticImageSearchService;

import java.util.List;

@Service
public class ElasticImageSearchFacadeImpl implements ElasticImageSearchFacade {

    private final ElasticImageSearchService elasticImageSearchService;

    public ElasticImageSearchFacadeImpl(ElasticImageSearchService elasticImageSearchService) {
        this.elasticImageSearchService = elasticImageSearchService;
    }

    @Override
    public List<String> searchImageGameName(String query) {
        return elasticImageSearchService.searchImageGameName(query);
    }
}
