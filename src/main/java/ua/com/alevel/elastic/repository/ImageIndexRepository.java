package ua.com.alevel.elastic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ua.com.alevel.elastic.document.ImageIndex;

@Repository
public interface ImageIndexRepository extends ElasticsearchRepository<ImageIndex, String> { }
