package com.springcomics.api.repository;

import com.springcomics.api.admin.adminDto.CommentDto;
import com.springcomics.api.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommentRepositoryCustom {

    Slice<CommentResponse> findSliceByComicBodyId(Long comicBodyId, Pageable pageable);

    Page<CommentDto> findCommentDtosByComicBodyId(Pageable pageable, Long comicBodyId);
}
