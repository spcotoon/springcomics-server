package com.springcomics.api.repository.comic;

import com.springcomics.api.admin.adminDto.ComicBodyDto;
import com.springcomics.api.domain.comic.ComicBody;
import com.springcomics.api.response.OneComicBodyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComicBodyRepositoryCustom {

    Page<ComicBody> findOneComicListByComicHeadId(Long comicHeadId, Pageable pageable);

    Long findMaxNoByComicHeadId(Long comicHeadId);

    OneComicBodyResponse findOneComicByComicHeadIdAndNo(Long comicHeadId, Long no);

    Page<ComicBodyDto> findComicBodyWithCommentByComicHeadId(Pageable pageable, Long comicHeadId);
}
