package com.sparta.elasticapi.elasticsearch.domain.post.repository;

import com.sparta.elasticapi.elasticsearch.domain.post.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, String> {
    List<PostDocument> findByTitleContainingOrContentContaining(String title, String content);
}
