package com.sns.elasticsearch.domain.post.repository;

import com.sns.elasticsearch.domain.post.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, String> {
    List<PostDocument> findByTitleContainingOrContentContaining(String title, String content);
}
