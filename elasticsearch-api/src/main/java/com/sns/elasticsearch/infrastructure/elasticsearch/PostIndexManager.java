package com.sns.elasticsearch.infrastructure.elasticsearch;

import com.sns.elasticsearch.domain.post.document.PostDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostIndexManager {

    private final ElasticsearchOperations elasticsearchOperations;

    public void index(PostDocument document) {
        elasticsearchOperations.save(document);
    }

    public void delete(String id) {
        elasticsearchOperations.delete(id, PostDocument.class);
    }
}
