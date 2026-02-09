package com.sparta.postapi.service;

import com.sparta.postapi.dto.PostCreateRequest;
import com.sparta.postapi.dto.PostResponse;
import com.sparta.postapi.dto.PostUpdateRequest;
import com.sparta.postapi.entity.Post;
import com.sparta.postapi.global.exception.DomainException;
import com.sparta.postapi.global.exception.ErrorCode;
import com.sparta.postapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(PostCreateRequest request) {
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(request.getAuthorId())
                .build();

        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.POST_NOT_FOUND));

        post.update(request.getTitle(), request.getContent());

        return post;
    }

    @Transactional
    public Post deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.POST_NOT_FOUND));

        post.delete();

        return post;
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
