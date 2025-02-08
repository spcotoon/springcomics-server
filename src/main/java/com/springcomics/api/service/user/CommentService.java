package com.springcomics.api.service.user;

import com.springcomics.api.config.util.CustomUser;
import com.springcomics.api.domain.comic.ComicBody;
import com.springcomics.api.domain.comic.ComicComment;
import com.springcomics.api.exception.ComicNotFound;
import com.springcomics.api.exception.UserNotLogin;
import com.springcomics.api.repository.CommentRepository;
import com.springcomics.api.repository.comic.ComicBodyRepository;
import com.springcomics.api.request.CommentPost;
import com.springcomics.api.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ComicBodyRepository comicBodyRepository;

    @Transactional
    public void postComment(Long comicBodyId, CommentPost commentPost) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotLogin();
        }

        CustomUser user = (CustomUser) authentication.getPrincipal();

        ComicBody comicBody = comicBodyRepository.findById(comicBodyId).orElseThrow(ComicNotFound::new);

        ComicComment comment = ComicComment.builder()
                .comment(commentPost.getComment())
                .comicBody(comicBody)
                .userNicknameOfComment(user.getDisplayName())
                .build();

        commentRepository.save(comment);
    }

    public Slice<CommentResponse> getComment(Long comicBodyId, Pageable pageable) {

        int pageSize = 20;
        Pageable requestPageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());

        return commentRepository.findSliceByComicBodyId(comicBodyId, requestPageable);
    }
}
