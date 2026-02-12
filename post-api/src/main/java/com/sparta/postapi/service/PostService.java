package com.sparta.postapi.service;

import com.sparta.postapi.dto.PostCreateRequest;
import com.sparta.postapi.dto.PostResponse;
import com.sparta.postapi.dto.PostUpdateRequest;
import com.sparta.postapi.entity.Post;
import com.sparta.postapi.event.PostCreatedEvent;
import com.sparta.postapi.event.PostDeletedEvent;
import com.sparta.postapi.event.PostUpdatedEvent;
import com.sparta.postapi.global.exception.DomainException;
import com.sparta.postapi.global.exception.ErrorCode;
import com.sparta.postapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long createPost(PostCreateRequest request) {
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(request.getAuthorId())
                .build();

        Post savedPost = postRepository.save(post);

        eventPublisher.publishEvent(PostCreatedEvent.from(savedPost));

        return savedPost.getId();
    }

    @Transactional
    public Long updatePost(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.POST_NOT_FOUND));

        post.update(request.getTitle(), request.getContent());

        eventPublisher.publishEvent(PostUpdatedEvent.from(post));

        return post.getId();
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.POST_NOT_FOUND));

        post.delete();

        eventPublisher.publishEvent(PostDeletedEvent.from(post));
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.POST_NOT_FOUND));

        if (post.isDeleted()) {
            throw new DomainException(ErrorCode.POST_NOT_FOUND);
        }

        return PostResponse.from(post);
    }

    public Page<PostResponse> getPosts(Pageable pageable) {
        return postRepository.findAllByDeletedAtIsNull(pageable)
                .map(PostResponse::from);
    }
}
